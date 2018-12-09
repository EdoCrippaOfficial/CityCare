package inc.elevati.imycity.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import inc.elevati.imycity.R;

public class ProgressDialog extends DialogFragment {

    public static ProgressDialog newInstance(int stringId) {
        ProgressDialog dialog = new ProgressDialog();
        Bundle args = new Bundle();
        args.putInt("string_id", stringId);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setCancelable(false);
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.dialog_progress);
        TextView tvLoading = dialog.findViewById(R.id.tv_loading);
        int id = getArguments().getInt("string_id");
        tvLoading.setText(id);
        return dialog;
    }

    @Override
    public void dismiss() {
        Dialog dialog = getDialog();
        if (dialog != null && dialog.isShowing()) super.dismiss();
    }
}
