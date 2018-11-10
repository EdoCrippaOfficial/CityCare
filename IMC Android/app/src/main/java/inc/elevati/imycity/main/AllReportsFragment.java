package inc.elevati.imycity.main;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import inc.elevati.imycity.R;
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
    public void addReports(List<Report> reports) {
        reportsAdapter.addReports(reports);
    }

    @Override
    public void updateReports(List<Report> reports) {
        reportsAdapter.updateReports(reports);
    }

    @Override
    public void showReportDialog(Report report) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_report);
        TextView tv_title = dialog.findViewById(R.id.tv_title);
        TextView tv_desc = dialog.findViewById(R.id.tv_desc);
        ImageView iv_image = dialog.findViewById(R.id.iv_image);
        final ProgressBar pb_loading = dialog.findViewById(R.id.pb_loading);
        tv_title.setText(report.getTitle());
        tv_desc.setText(report.getDescription());
        pb_loading.setVisibility(View.VISIBLE);
        GlideApp.with(this)
                .load(presenter.getImageReference(report.getImageName(), "img"))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        pb_loading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(iv_image);

        dialog.show();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        Window window = dialog.getWindow();
        if (window == null)
            return;
        window.setLayout(width * 9/10, window.getAttributes().height);
    }

    @Override
    public void resetRefreshing() {
        refresher.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        presenter.loadAllReports();
    }
}
