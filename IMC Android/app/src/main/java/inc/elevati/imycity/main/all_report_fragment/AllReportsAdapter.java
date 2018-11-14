package inc.elevati.imycity.main.all_report_fragment;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import inc.elevati.imycity.R;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.GlideApp;
import inc.elevati.imycity.utils.Report;

class AllReportsAdapter extends RecyclerView.Adapter<AllReportsAdapter.MyViewHolder> {

    private Fragment context;
    private List<Report> reports;
    private MainContracts.AllReportsPresenter presenter;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pb_loading;
        TextView tv_title, tv_desc;
        ImageView iv_image;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.pb_loading = itemView.findViewById(R.id.pb_loading);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_desc = itemView.findViewById(R.id.tv_desc);
            this.iv_image = itemView.findViewById(R.id.iv_image);
        }
    }

    AllReportsAdapter(Fragment context, MainContracts.AllReportsPresenter presenter) {
        this.reports = new ArrayList<>();
        this.context = context;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public AllReportsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.showReport(reports.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Report r = reports.get(position);
        holder.tv_title.setText(r.getTitle());
        holder.tv_desc.setText(r.getDescription());
        holder.pb_loading.setVisibility(View.VISIBLE);
        GlideApp.with(context)
                .load(r.getImageReference(Report.IMAGE_THUMBNAIL))
                .placeholder(R.drawable.ic_image)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.pb_loading.setVisibility(View.GONE);
                        holder.iv_image.setImageResource(R.drawable.ic_no_image);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.pb_loading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.iv_image);
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    void sortReports(int order) {
        switch (order) {
            case AllReportsFragment.REPORT_SORT_DATE_NEWEST:
                Collections.sort(reports, new Comparator<Report>() {
                    @Override
                    public int compare(Report r1, Report r2) {
                        return Long.compare(r2.getTimestamp(), r1.getTimestamp());
                    }
                });
                break;
            case AllReportsFragment.REPORT_SORT_DATE_OLDEST:
                Collections.sort(reports, new Comparator<Report>() {
                    @Override
                    public int compare(Report r1, Report r2) {
                        return Long.compare(r1.getTimestamp(), r2.getTimestamp());
                    }
                });
                break;
            case AllReportsFragment.REPORT_SORT_STARS_MORE:
                break;
            case AllReportsFragment.REPORT_SORT_STARS_LESS:
                break;
        }
        notifyDataSetChanged();
    }

    void updateReports(List<Report> reports, int order) {
        this.reports.clear();
        this.reports.addAll(reports);
        sortReports(order);
    }
}
