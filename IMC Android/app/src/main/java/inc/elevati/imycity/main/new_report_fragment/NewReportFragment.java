package inc.elevati.imycity.main.new_report_fragment;

import android.app.Dialog;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.media.ExifInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import inc.elevati.imycity.R;
import inc.elevati.imycity.main.MainContracts;

import static android.app.Activity.RESULT_OK;

/**
 * This fragment contains the report creating form; It also handles
 * the database sending delegating it to its presenter
 */
public class NewReportFragment extends Fragment implements MainContracts.NewReportView {

    /**
     * ViewModel subclass to preserve the loaded image and photo path across
     * orientation changes and activity re-creation; the data is lost when
     * activity is destroyed by system or user
     */
    public static class PersistentData extends ViewModel {
        String cameraPath;
        Bitmap image;
    }

    /**
     * Constants used to send intent requests (image pick or camera)
     */
    private static final int PICK_IMAGE_REQUEST = 71;
    private static final int TAKE_PHOTO_REQUEST = 45;

    /**
     * This view presenter, called to handle non-graphic requests
     */
    private MainContracts.NewReportPresenter presenter;

    /**
     * Dialog displayed during database and storage sending
     */
    private Dialog progressDialog;

    /**
     * This fragment views: imageView for the loaded report image
     * and TextInputLayout for title and description
     */
    private ImageView imageView;
    private TextInputEditText textInputTitle, textInputDesc;

    /**
     * The loaded image data (Bitmap), stored in a PersistentData instance
     * to preserve it during orientation changes and activity re-creation
     */
    private PersistentData imageData;

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
        setHasOptionsMenu(true);
        presenter = new NewReportPresenter(this);
        imageView = v.findViewById(R.id.iv_new_report);
        textInputTitle = v.findViewById(R.id.text_input_edit_title);
        textInputDesc = v.findViewById(R.id.text_input_edit_desc);
        final TextInputLayout inputLayoutTitle = v.findViewById(R.id.text_input_layout_title);
        final TextInputLayout inputLayoutDesc = v.findViewById(R.id.text_input_layout_desc);

        // Restore the image if it was set
        imageData = ViewModelProviders.of(this).get(PersistentData.class);
        if (imageData.image != null) imageView.setImageBitmap(imageData.image);

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
                inputLayoutTitle.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
        textInputDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputLayoutDesc.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        // Send report button
        v.findViewById(R.id.bn_new_report_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageData.image == null) {
                    Toast.makeText(getContext(), R.string.new_report_no_picture, Toast.LENGTH_SHORT).show();
                    Animation animation = new AlphaAnimation(1f, 0f);
                    animation.setDuration(250);
                    animation.setRepeatCount(2);
                    imageView.startAnimation(animation);
                    return;
                } else if (textInputTitle.getText().toString().isEmpty()) {
                    inputLayoutTitle.setError(getString(R.string.new_report_no_title));
                    return;
                } else if (textInputDesc.getText().toString().isEmpty()) {
                    inputLayoutDesc.setError(getString(R.string.new_report_no_description));
                    return;
                }
                // Progress Dialog
                progressDialog = new Dialog(getContext());
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setContentView(R.layout.dialog_progress);
                progressDialog.setCancelable(false);
                progressDialog.show();

                presenter.handleSendReport(imageData.image, textInputTitle.getText().toString(), textInputDesc.getText().toString());
            }
        });
        return v;
    }

    /**
     * Static method that returns an instance of this fragment
     * @return a NewReportFragment instance
     */
    public static NewReportFragment newInstance() {
        return new NewReportFragment();
    }

    /**
     * Dismisses the progress dialog after a report sending
     * @param error should be true if the operation didn't complete
     *              If false the fragments fields (ImageView and EditText
     *              for title and description) are cleared
     */
    @Override
    public void dismissProgressDialog(boolean error) {
        FragmentActivity activity = getActivity();
        if (activity == null || activity.isDestroyed()) return;
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        if (error) {
            Toast.makeText(getContext(), R.string.new_report_error, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), R.string.new_report_ok, Toast.LENGTH_LONG).show();
            imageData.image = null;
            imageView.setImageResource(R.drawable.ic_add_image);
            textInputDesc.setText(null);
            textInputTitle.setText(null);
        }
    }

    /**
     * Creates a dialog that asks user where to pick the image (gallery or camera)
     */
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

    /**
     * Starts activity to pick image from gallery
     */
    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.new_report_select_picture)), PICK_IMAGE_REQUEST);
    }

    /**
     * Creates a File object to receive photo from camera and then start camera activity
     */
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
                InputStream inputStream = getContext().getContentResolver().openInputStream(fileUri);
                imageData.image = fixBitmapRotation(image, inputStream);
                imageView.setImageBitmap(imageData.image);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), R.string.new_report_pick_error, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            File file = new File(imageData.cameraPath);
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));
                imageData.image = fixBitmapRotation(image, new FileInputStream(file));
                imageView.setImageBitmap(imageData.image);
            } catch (IOException e) {
                Toast.makeText(getContext(), R.string.new_report_camera_error, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Method called to fix image rotation issue
     * @param image the image to rotate
     * @param stream the image input stream
     * @return the rotated Bitmap
     * @throws IOException error accessing image path
     */
    private Bitmap fixBitmapRotation(Bitmap image, InputStream stream) throws IOException {
        ExifInterface exifInterface = new ExifInterface(stream);
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix;
        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix = new Matrix();
                matrix.postRotate(90);
                return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);

            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix = new Matrix();
                matrix.postRotate(180);
                return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);

            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix = new Matrix();
                matrix.postRotate(270);
                return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                return image;
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
        imageData.cameraPath = image.getAbsolutePath();
        return image;

    }

    /**
     * Method called when action bar is created. In this fragment the sort button is
     * retrieved and then is moved off the screen with an animation
     * @param menu the menu Object
     * @param inflater the layout inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Animate the sort button out of the screen
        inflater.inflate(R.menu.menu_bar, menu);
        MenuItem sortButton = menu.findItem(R.id.bn_sort);
        ImageView imageSort = (ImageView) getLayoutInflater().inflate(R.layout.button_sort, null);
        sortButton.setActionView(imageSort);
        TranslateAnimation animate = new TranslateAnimation(0, 300, 0, 0);
        animate.setDuration(450);
        animate.setFillAfter(true);
        sortButton.getActionView().startAnimation(animate);
        sortButton.getActionView().setEnabled(false);
    }
}