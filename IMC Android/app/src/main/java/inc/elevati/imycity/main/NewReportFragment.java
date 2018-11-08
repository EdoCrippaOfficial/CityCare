package inc.elevati.imycity.main;

import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import inc.elevati.imycity.R;

import static android.app.Activity.RESULT_OK;

public class NewReportFragment extends Fragment implements MainContracts.NewReportView {

    private static final int PICK_IMAGE_REQUEST = 71;
    private MainContracts.NewReportPresenter presenter;
    private Dialog progressDialog;
    private Uri filePath;
    private ImageView imageView;
    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_new, container, false);
        presenter = new NewReportPresenter(this);
        imageView = v.findViewById(R.id.iv_new_report);
        final EditText et_title = v.findViewById(R.id.et_new_report_title);
        final EditText et_desc = v.findViewById(R.id.et_new_report_desc);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        v.findViewById(R.id.bn_new_report_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath == null) {
                    Toast.makeText(getContext(), "Insert a picture!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Progress Dialog
                progressDialog = new Dialog(getContext());
                progressDialog.setContentView(R.layout.dialog_progress);
                progressDialog.setCancelable(false);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                presenter.handleSendReport(bitmap, et_title.getText().toString(), et_desc.getText().toString());
            }
        });
        return v;
    }

    public static NewReportFragment newInstance() {
        return new NewReportFragment();
    }

    @Override
    public void dismissProgressDialog() {
        FragmentActivity activity = getActivity();
        if (activity == null || activity.isDestroyed()) return;
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    private void pickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null ) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
