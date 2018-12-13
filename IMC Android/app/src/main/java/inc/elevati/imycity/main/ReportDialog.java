package inc.elevati.imycity.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.DateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import inc.elevati.imycity.R;
import inc.elevati.imycity.utils.GlideApp;
import inc.elevati.imycity.utils.Report;
import inc.elevati.imycity.utils.firebase.FirebaseAuthHelper;
import inc.elevati.imycity.utils.firebase.FirestoreDeleter;

/** In this class it is defined the style of the dialog shown when user clicks on a report */
public class ReportDialog extends DialogFragment {

    /**
     * Static method that returns an instance of ReportDialog
     * with the specified report as an argument
     * @param report the report to be shown
     * @return a ReportDialog instance
     */
    public static ReportDialog newInstance(Report report) {
        ReportDialog dialog = new ReportDialog();
        Bundle args = new Bundle();
        args.putParcelable("report", report);
        dialog.setArguments(args);
        return dialog;
    }

    /**
     * Called when the dialog is created, here it's specified its style
     * @param savedInstanceState a Bundle containing saved data to be restored
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
    }

    /**
     * Called when the dialog View is created, here all Views are
     * initialized and the report data is adapted to user interface
     * @param inflater the layout inflater
     * @param container this dialog parent
     * @param savedInstanceState a Bundle containing saved data to be restored
     * @return the created View
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.dialog_report, null);

        // Report retrieving from arguments
        final Report report = getArguments().getParcelable("report");
        TextView tv_title = v.findViewById(R.id.tv_title);
        TextView tv_desc = v.findViewById(R.id.tv_desc);
        TextView tv_date = v.findViewById(R.id.tv_date);
        Button bn_delete = v.findViewById(R.id.bn_delete);
        if (report.getUserId().equals(FirebaseAuthHelper.getUserId()))
            bn_delete.setVisibility(View.VISIBLE);
        bn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Cancellazione");
                builder.setMessage("Sei sicuro?");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirestoreDeleter.deleteReport(report.getId(), ReportDialog.this);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        final ImageView iv_image = v.findViewById(R.id.iv_report_image);
        final ProgressBar pb_loading = v.findViewById(R.id.pb_image);
        tv_title.setText(report.getTitle());
        tv_desc.setText(report.getDescription());

        // Time formatting: Created on: date, hour
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());
        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(getContext());
        Date date = new Date(report.getTimestamp());
        String completeDate = dateFormat.format(date) + ", " + timeFormat.format(date);
        tv_date.setText(getString(R.string.report_date, report.getUserName(), completeDate));
        pb_loading.setVisibility(View.VISIBLE);

        // Image loading from storage with Glide
        GlideApp.with(this)
                .load(report.getImageReference(Report.IMAGE_FULL))
                .placeholder(R.drawable.ic_image)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        pb_loading.setVisibility(View.GONE);
                        iv_image.setImageResource(R.drawable.ic_no_image);
                        return true;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        pb_loading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(iv_image);
        return v;
    }

    public void onReportDeleted(){
        this.dismiss();
        Toast.makeText(getContext(), "Segnalazione cancellata!, ricarica la pagina", Toast.LENGTH_LONG).show();
    }
}
