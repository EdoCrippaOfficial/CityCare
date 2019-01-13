package inc.elevati.imycity.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import inc.elevati.imycity.R;

/** Custom implementation of a progress dialog, built on top of {@link DialogFragment} */
public class ProgressDialog extends DialogFragment {

    /**
     * @param stringId the id of the string to show inside the dialog
     * @return an instance of ProgressDialog
     */
    public static ProgressDialog newInstance(int stringId) {
        ProgressDialog dialog = new ProgressDialog();
        Bundle args = new Bundle();
        args.putInt("string_id", stringId);
        dialog.setArguments(args);
        return dialog;
    }

    /**
     * Created when dialog is created, here the arguments are retrieved
     * @param savedInstanceState a Bundle containing saved data to be restored
     * @return the dialog created
     */
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

    /**
     * Custom implementation of {@link DialogFragment#dismiss()} method
     */
    @Override
    public void dismiss() {
        Dialog dialog = getDialog();
        if (dialog != null && dialog.isShowing()) super.dismissAllowingStateLoss();
    }
}
