package inc.elevati.imycity.main;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import inc.elevati.imycity.R;
import inc.elevati.imycity.firebase.FirestoreHelper;
import inc.elevati.imycity.utils.EspressoIdlingResource;
import inc.elevati.imycity.utils.GlideApp;
import inc.elevati.imycity.utils.ProgressDialog;
import inc.elevati.imycity.utils.Report;
import inc.elevati.imycity.firebase.FirebaseAuthHelper;

/** In this class it is defined the style of the dialog shown when user clicks on a report */
public class ReportDialog extends DialogFragment implements FirestoreHelper.onDeleteReportListener {

    private ProgressDialog progressDialog;

    /**
     * Static method that returns an instance of ReportDialog
     * with the specified report as an argument
     * @param report the report to be shown
     * @return a ReportDialog instance
     */
    public static ReportDialog newInstance(Report report, MainContracts.ReportListPresenter presenter) {
        ReportDialog dialog = new ReportDialog();
        Bundle args = new Bundle();
        args.putParcelable("report", report);
        args.putSerializable("presenter", presenter);
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
        TextView tv_status = v.findViewById(R.id.tv_status);
        TextView tv_title = v.findViewById(R.id.tv_title);
        TextView tv_desc = v.findViewById(R.id.tv_desc);
        TextView tv_reply = v.findViewById(R.id.tv_reply);
        TextView tv_date = v.findViewById(R.id.tv_date);
        Button bn_delete = v.findViewById(R.id.bn_delete);
        MapView mapView = v.findViewById(R.id.map_view);

        // Show report status
        switch (report.getStatus()) {
            case Report.STATUS_ACCEPTED:
                tv_status.setText(R.string.report_accepted);
                tv_status.setBackgroundColor(getContext().getResources().getColor(R.color.color_primary));
                break;
            case Report.STATUS_REFUSED:
                tv_status.setText(R.string.report_refused);
                tv_status.setBackgroundColor(getContext().getResources().getColor(R.color.red));
                break;
            case Report.STATUS_COMPLETED:
                tv_status.setText(R.string.report_completed);
                tv_status.setBackgroundColor(getContext().getResources().getColor(R.color.green));
                break;
            case Report.STATUS_WAITING:
                tv_status.setText(R.string.report_waiting);
                tv_status.setBackgroundColor(getContext().getResources().getColor(R.color.grey));
                break;
        }

        // Show delete button if current user is the creator of a waiting report
        if (report.getUserId().equals(FirebaseAuthHelper.getUserId()) && report.getStatus() == Report.STATUS_WAITING)
            bn_delete.setVisibility(View.VISIBLE);

        bn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_confirm);
                dialog.findViewById(R.id.bn_delete_no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.bn_delete_yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EspressoIdlingResource.increment();
                        dialog.dismiss();
                        showProgressDialog();
                        FirestoreHelper helper = new FirestoreHelper(ReportDialog.this);
                        helper.deleteReport(report.getId());
                    }
                });
                dialog.show();
            }
        });
        final ImageView iv_image = v.findViewById(R.id.iv_report_image);
        final ProgressBar pb_loading = v.findViewById(R.id.pb_image);
        tv_title.setText(report.getTitle());
        tv_desc.setText(report.getDescription());

        final String reply = report.getReply();
        if (reply != null && !reply.equals("")) {
            v.findViewById(R.id.container_reply).setVisibility(View.VISIBLE);
            tv_reply.setText(reply);
        }

        // Time formatting: Created on: date, hour
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());
        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(getContext());
        Date date = new Date(report.getTimestamp());
        String completeDate = dateFormat.format(date) + ", " + timeFormat.format(date);
        tv_date.setText(getString(R.string.report_date, report.getUserName(), completeDate));
        pb_loading.setVisibility(View.VISIBLE);

        // Map creating
        if (report.getPosition() != null) {
            mapView.setVisibility(View.VISIBLE);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    LatLng position = new LatLng(report.getPosition().getLatitude(), report.getPosition().getLongitude());
                    mMap.addMarker(new MarkerOptions().position(position));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 17f));
                }
            });
        }

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

    /** Method called to show a non-cancelable progress dialog during database operations */
    private void showProgressDialog() {
        progressDialog = ProgressDialog.newInstance(R.string.report_deleting);
        progressDialog.show(getFragmentManager(), "progress");
    }

    /** Dismisses the progress dialog after a report deleting */
    private void dismissProgressDialog() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void onReportDeleted() {
        dismissProgressDialog();
        dismiss();
        MainContracts.ReportListPresenter presenter = (MainContracts.ReportListPresenter) getArguments().getSerializable("presenter");
        EspressoIdlingResource.decrement();
        presenter.loadReports();
    }

    @Override
    public void onReportDeleteFailed() {
        dismissProgressDialog();
        Toast.makeText(getContext(), R.string.report_deleting_fail, Toast.LENGTH_SHORT).show();
        EspressoIdlingResource.decrement();
    }

    @Override
    public void onPause() {
        this.dismiss();
        super.onPause();
    }
}
