package inc.elevati.imycity.main.new_report_fragment;

import android.app.Dialog;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import inc.elevati.imycity.R;
import inc.elevati.imycity.main.MainContracts;

import static android.app.Activity.RESULT_OK;

public class NewReportFragment extends Fragment implements MainContracts.NewReportView {

    private static final int PICK_IMAGE_REQUEST = 71;
    private MainContracts.NewReportPresenter presenter;
    private Dialog progressDialog;
    private Uri filePath;
    private ImageView imageView;
    private EditText et_title, et_desc;
    private PersistentData imageData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_new, container, false);
        presenter = new NewReportPresenter(this);
        imageView = v.findViewById(R.id.iv_new_report);
        et_title = v.findViewById(R.id.et_new_report_title);
        et_desc = v.findViewById(R.id.et_new_report_desc);

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

        v.findViewById(R.id.bn_new_report_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageData.image == null) {
                    Toast.makeText(getContext(), R.string.new_report_no_picture, Toast.LENGTH_SHORT).show();
                    return;
                } else if (et_title.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), R.string.new_report_no_title, Toast.LENGTH_SHORT).show();
                    return;
                } else if (et_desc.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), R.string.new_report_no_description, Toast.LENGTH_SHORT).show();
                    return;
                }
                // Progress Dialog
                progressDialog = new Dialog(getContext());
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setContentView(R.layout.dialog_progress);
                progressDialog.setCancelable(false);
                progressDialog.show();

                presenter.handleSendReport(imageData.image, et_title.getText().toString(), et_desc.getText().toString());
            }
        });
        return v;
    }

    public static NewReportFragment newInstance() {
        return new NewReportFragment();
    }

    @Override
    public void dismissProgressDialog(boolean error) {
        FragmentActivity activity = getActivity();
        if (activity == null || activity.isDestroyed()) return;
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        if (error) {
            Toast.makeText(getContext(), R.string.new_report_error, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), R.string.new_report_ok, Toast.LENGTH_LONG).show();
            filePath = null;
            imageData.image = null;
            imageView.setImageResource(R.drawable.ic_add_image);
            et_desc.setText(null);
            et_title.setText(null);
        }
    }

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

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.new_report_select_picture)), PICK_IMAGE_REQUEST);
    }

    private void takePicture() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null ) {
            filePath = data.getData();
            try {
                imageData.image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(imageData.image);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class PersistentData extends ViewModel {
        Bitmap image;
    }
}
