package inc.elevati.imycity.main.newreport;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import inc.elevati.imycity.R;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.GlideApp;

import static android.app.Activity.RESULT_OK;

/**
 * This fragment contains the report creating form; It also handles
 * the database sending delegating it to its presenter
 */
public class NewReportFragment extends Fragment implements MainContracts.NewReportView {

    /** Constants used to send intent requests (image pick or camera) */
    private static final int PICK_IMAGE_REQUEST = 71;
    private static final int TAKE_PHOTO_REQUEST = 45;

    /** This view presenter, called to handle non-graphic requests*/
    public MainContracts.NewReportPresenter presenter;

    /** Dialog displayed during database and storage sending */
    private Dialog progressDialog;

    /** Global toast reference to avoid toasts accumulation */
    private Toast toast;

    /**
     * This fragment views: imageView for the loaded report image
     * and TextInputLayout for title and description
     */
    private ImageView imageView;
    private TextInputEditText textInputTitle, textInputDesc;
    private TextInputLayout textLayoutTitle, textLayoutDesc;

    /** Uri referencing the image loaded by the user */
    private Uri imageUri;

    /**
     * Static method that returns an instance of this fragment,
     * should be called only by the ViewPager adapted
     * @return a NewReportFragment instance
     */
    public static NewReportFragment newInstance() {
        return new NewReportFragment();
    }

    /**
     * Method called when the View associated to this fragment is created (the first time this
     * fragment is shown, at orientation changes, at activity re-creations...); Here the layout
     * is inflated and all Views owned by this fragment are initialized
     * @param inflater the layout inflater
     * @param container this fragment parent view
     * @param savedInstanceState a Bundle containing saved data to be restored
     * @return the View initialized and inflated
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_new, container, false);
        presenter = new NewReportPresenter(this);
        imageView = v.findViewById(R.id.iv_new_report);
        textInputTitle = v.findViewById(R.id.text_input_edit_title);
        textInputDesc = v.findViewById(R.id.text_input_edit_desc);
        textLayoutTitle = v.findViewById(R.id.text_input_layout_title);
        textLayoutDesc = v.findViewById(R.id.text_input_layout_desc);

        // Restore the image uri if it was set and progress dialog if it was shown
        if (savedInstanceState != null) {

            // Progress dialog check
            if (savedInstanceState.getBoolean("progress_dialog")) showProgressDialog();

            // Image uri check
            imageUri = savedInstanceState.getParcelable("uri");
            if (imageUri != null) {

                // Reload the image
                GlideApp.with(this).load(imageUri)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                // If error occurs during image restoring, delete the saved file reference
                                imageUri = null;
                                imageView.setImageResource(R.drawable.ic_add_image);
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(imageView);
            }
        }

        // Click listener to pick or take a picture
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createImageDialog();
            }
        });

        // Clear error when users provides input in TextInputEditTexts
        textInputTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textLayoutTitle.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
        textInputDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textLayoutDesc.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        // Send report button
        v.findViewById(R.id.bn_new_report_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = textInputTitle.getText().toString();
                String desc = textInputDesc.getText().toString();
                presenter.sendButtonClicked(title, desc, getActivity().getApplicationContext(), imageUri);
            }
        });
        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("uri", imageUri);
        if (progressDialog != null && progressDialog.isShowing()) outState.putBoolean("progress_dialog", true);
    }

    @Override
    public void showProgressDialog() {
        progressDialog = new Dialog(getContext());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void notifyInvalidImage() {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(getContext(), R.string.new_report_no_picture, Toast.LENGTH_SHORT);
        toast.show();

        // Blink animation of the ImageView (the camera image)
        Animation animation = new AlphaAnimation(1f, 0.4f);
        animation.setDuration(350);
        animation.setRepeatCount(2);
        imageView.startAnimation(animation);
    }

    @Override
    public void notifyInvalidTitle() {
        textLayoutTitle.setError(getString(R.string.new_report_no_title));
    }

    @Override
    public void notifyInvalidDescription() {
        textLayoutDesc.setError(getString(R.string.new_report_no_description));
    }

    /**
     * Dismisses the progress dialog after a report sending
     * @param error should be true if the operation didn't complete
     *              If false the fragments fields (ImageView and EditText
     *              for title and description) are cleared
     */
    @Override
    public void dismissProgressDialog(final boolean error) {
        FragmentActivity activity = getActivity();
        if (activity == null || activity.isDestroyed()) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
                if (error) {
                    Toast.makeText(getContext(), R.string.new_report_error, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), R.string.new_report_ok, Toast.LENGTH_LONG).show();
                    imageView.setImageResource(R.drawable.ic_add_image);
                    imageUri = null;
                    textInputDesc.setText(null);
                    textInputTitle.setText(null);
                }
            }
        });
    }

    /** Creates a dialog that asks user where to pick the image (gallery or camera) */
    private void createImageDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_pick);
        dialog.findViewById(R.id.bn_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.bn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /** Starts activity to pick image from gallery */
    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.new_report_select_picture)), PICK_IMAGE_REQUEST);
    }

    /** Creates a File object to receive photo from camera and then start camera activity */
    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                // Create the File where the photo should go
                File cameraFile = createImageFile();
                Uri photoURI = FileProvider.getUriForFile(getContext(), "inc.elevati.imycity.fileprovider", cameraFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST);
            } catch (IOException ex) {
                Toast.makeText(getContext(), R.string.new_report_camera_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), R.string.new_report_camera_missing, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Called after the pick image or camera activity has completed
     * @param requestCode the request code, can be PICK_IMAGE_REQUEST or TAKE_PHOTO_REQUEST
     * @param resultCode the activity result code, it must be RESULT_OK to continue
     * @param data an Intent containing the returned data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Retrieving image from storage
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            GlideApp.with(this).load(imageUri).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    // Some file managers require read storage permission
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Asking for read permission and then try again in onRequestPermissionsResult
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
                    } else {
                        // Unknown error
                        imageView.setImageResource(R.drawable.ic_add_image);
                        Toast.makeText(getContext(), R.string.new_report_pick_error, Toast.LENGTH_LONG).show();
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(imageView);

        // Getting camera result
        } else if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            GlideApp.with(this).load(imageUri)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setImageResource(R.drawable.ic_add_image);
                        Toast.makeText(getContext(), R.string.new_report_camera_error, Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
        }
    }

    /**
     * Called when user has allowed or denied to grant READ_EXTERNAL_STORAGE permission, if
     * result is positive we try again to load image, otherwise error is displayed
     * @param requestCode the request code passed in requestPermissions
     * @param permissions the permissions requested
     * @param grantResults the request result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, try again to load image
            GlideApp.with(this).load(imageUri).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    // Error occurred even with permissions, error message displayed
                    Toast.makeText(getContext(), R.string.new_report_pick_error, Toast.LENGTH_LONG).show();
                    imageView.setImageResource(R.drawable.ic_add_image);
                    return true;
                }
                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(imageView);
        } else {
            // Permission is denied, error message displayed
            imageView.setImageResource(R.drawable.ic_add_image);
            Toast.makeText(getContext(), R.string.new_report_permissions, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Creates a file used to receive the camera image
     * @return the File object created
     * @throws IOException exception occurred during file creation
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imageUri = Uri.fromFile(image);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        getActivity().sendBroadcast(mediaScanIntent);
        return image;
    }
}