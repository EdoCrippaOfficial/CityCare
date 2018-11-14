package inc.elevati.imycity.main.all_report_fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import inc.elevati.imycity.R;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.GlideApp;
import inc.elevati.imycity.utils.Report;

public class AllReportsFragment extends Fragment implements MainContracts.AllReportsView, SwipeRefreshLayout.OnRefreshListener {

    private AllReportsAdapter reportsAdapter;
    private MainContracts.AllReportsPresenter presenter;
    private SwipeRefreshLayout refresher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all, container, false);

        // Swipe refresh
        refresher = v.findViewById(R.id.refresher);
        refresher.setOnRefreshListener(this);

        // RecyclerView
        RecyclerView recyclerView = v.findViewById(R.id.recycler_all_reports);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        presenter = new AllReportsPresenter(this);
        reportsAdapter = new AllReportsAdapter(this, presenter);
        recyclerView.setAdapter(reportsAdapter);

        // Load all reports
        refresher.setRefreshing(true);
        presenter.loadAllReports();
        return v;
    }

    public static AllReportsFragment newInstance() {
        return new AllReportsFragment();
    }


    @Override
    public void updateReports(List<Report> reports) {
        reportsAdapter.updateReports(reports);
    }

    @Override
    public void showReportDialog(Report report) {
        ReportDialog dialog = ReportDialog.newInstance(report);
        dialog.show(getFragmentManager(), null);
    }

    @Override
    public void resetRefreshing() {
        refresher.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        presenter.loadAllReports();
    }

    public static class ReportDialog extends DialogFragment {

        static ReportDialog newInstance(Report report) {
            ReportDialog dialog = new ReportDialog();
            Bundle args = new Bundle();
            args.putParcelable("report", report);
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(STYLE_NO_TITLE, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.dialog_report, container, false);
            Report report = getArguments().getParcelable("report");
            TextView tv_title = v.findViewById(R.id.tv_title);
            TextView tv_desc = v.findViewById(R.id.tv_desc);
            TextView tv_date = v.findViewById(R.id.tv_date);
            final PhotoView iv_image = v.findViewById(R.id.iv_image);
            final ProgressBar pb_loading = v.findViewById(R.id.pb_loading);
            tv_title.setText(report.getTitle());
            tv_desc.setText(report.getDescription());
            DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());
            DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(getContext());
            Date date = new Date(report.getTimestamp());
            String completeDate = dateFormat.format(date) + ", " + timeFormat.format(date);
            tv_date.setText(getString(R.string.report_date, completeDate));
            pb_loading.setVisibility(View.VISIBLE);
            GlideApp.with(this)
                    .load(report.getImageReference(Report.ImageType.FULL))
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
    }
}
