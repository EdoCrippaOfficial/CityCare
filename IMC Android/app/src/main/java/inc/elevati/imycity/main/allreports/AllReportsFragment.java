package inc.elevati.imycity.main.allreports;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import inc.elevati.imycity.R;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.GlideApp;
import inc.elevati.imycity.utils.Report;

/** This fragment shows all reports retrieved from the database */
public class AllReportsFragment extends Fragment implements MainContracts.AllReportsView, SwipeRefreshLayout.OnRefreshListener {

    /** Sorts reports by their creation date from the newest to the oldest */
    final static int REPORT_SORT_DATE_NEWEST = 1;

    /** Sorts reports by their creation date from the oldest to the newest */
    final static int REPORT_SORT_DATE_OLDEST = 2;

    /** Sorts reports by the number of stars from the most popular to the least */
    final static int REPORT_SORT_STARS_MORE = 3;

    /** Sorts reports by the number of stars from the least popular to the most */
    final static int REPORT_SORT_STARS_LESS = 4;

    /** The sorting criteria chosen */
    private static int sort_criteria;

    /** The RecyclerView adapter */
    private AllReportsAdapter reportsAdapter;

    /** Presenter that handles non graphics-related requests */
    private MainContracts.AllReportsPresenter presenter;

    /** Object used to refresh the report list */
    private SwipeRefreshLayout refresher;

    /**
     * Method called when the View associated to this fragment is created (the first time this
     * fragment is shown, at orientation changes, at activity re-creations...); here the layout
     * is inflated and all Views owned by this fragment are initialized. In addition
     * SharedPreferences are read to retrieve the preferred report sorting criteria
     * @param inflater the layout inflater
     * @param container this fragment parent view
     * @param savedInstanceState a Bundle containing saved data to be restored
     * @return the View initialized and inflated
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all, container, false);
        setHasOptionsMenu(true);

        // Shared preferences for report sorting criteria
        if (savedInstanceState == null) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("IMC", Context.MODE_PRIVATE);
            sort_criteria = sharedPreferences.getInt("sort", REPORT_SORT_DATE_NEWEST);
        }

        // Swipe refresh
        refresher = v.findViewById(R.id.refresher);
        refresher.setOnRefreshListener(this);
        refresher.setColorSchemeResources(R.color.color_primary);

        // RecyclerView
        RecyclerView recyclerView = v.findViewById(R.id.recycler_all_reports);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Presenter and adapter creation
        presenter = new AllReportsPresenter(this);
        reportsAdapter = new AllReportsAdapter(this, presenter);
        recyclerView.setAdapter(reportsAdapter);

        // Load all reports
        refresher.setRefreshing(true);
        presenter.loadAllReports();
        return v;
    }

    /**
     * Static method that returns an instance of this fragment
     * @return a NewReportFragment instance
     */
    public static AllReportsFragment newInstance() {
        return new AllReportsFragment();
    }

    /**
     * Method called to update the report list displayed in RecyclerView
     * @param reports the list of reports to be displayed
     */
    @Override
    public void updateReports(List<Report> reports) {
        reportsAdapter.updateReports(reports, sort_criteria);
    }

    /**
     * Called when user clicks on a report in the list, it opens a
     * fullscreen dialog containing all the report information
     * @param report the clicked report
     */
    @Override
    public void showReportDialog(Report report) {
        ReportDialog dialog = ReportDialog.newInstance(report);
        dialog.show(getFragmentManager(), null);
    }

    /** Method called to hide the View shown when refreshing */
    @Override
    public void resetRefreshing() {
        refresher.setRefreshing(false);
    }

    /** Called when user swipes down on screen to refresh list */
    @Override
    public void onRefresh() {
        presenter.loadAllReports();
    }

    /**
     * Method called when action bar is created. In this fragment the sort button is
     * retrieved and then is moved on the screen with an animation, and a listener
     * is attached to it to allow user to change the report sort criteria
     * @param menu the menu Object
     * @param inflater the layout inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Set a click listener on button created and animated in from Activity
        MenuItem sortButton = menu.findItem(R.id.bn_sort);
        sortButton.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dialog that prompts user the sort criteria selection
                final AppCompatDialog dialog = new AppCompatDialog(getContext());
                dialog.setTitle(R.string.sort_report_title);
                dialog.setContentView(R.layout.dialog_sort);

                // Set the button selected by default (based on previous choice)
                RadioGroup radioGroup = dialog.findViewById(R.id.radio_sort);
                int idChecked = R.id.bn_newest;
                switch (sort_criteria) {
                    case AllReportsFragment.REPORT_SORT_DATE_NEWEST:
                        idChecked = R.id.bn_newest;
                        break;
                    case AllReportsFragment.REPORT_SORT_DATE_OLDEST:
                        idChecked = R.id.bn_oldest;
                        break;
                    case AllReportsFragment.REPORT_SORT_STARS_MORE:
                        idChecked = R.id.bn_more_stars;
                        break;
                    case AllReportsFragment.REPORT_SORT_STARS_LESS:
                        idChecked = R.id.bn_less_stars;
                        break;
                }
                radioGroup.check(idChecked);

                // Listeners for dialog buttons
                dialog.findViewById(R.id.bn_newest).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (sort_criteria != REPORT_SORT_DATE_NEWEST) changeSortCriteria(REPORT_SORT_DATE_NEWEST);
                    }
                });
                dialog.findViewById(R.id.bn_oldest).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (sort_criteria != REPORT_SORT_DATE_OLDEST) changeSortCriteria(REPORT_SORT_DATE_OLDEST);
                    }
                });
                dialog.findViewById(R.id.bn_more_stars).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (sort_criteria != REPORT_SORT_STARS_MORE) changeSortCriteria(REPORT_SORT_STARS_MORE);
                    }
                });
                dialog.findViewById(R.id.bn_less_stars).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (sort_criteria != REPORT_SORT_STARS_LESS) changeSortCriteria(REPORT_SORT_STARS_LESS);
                    }
                });
                dialog.show();
            }
        });
    }

    /**
     * Tells the adapter the sort criteria for the reports and then saves it in SharedPreferences
     * @param sortCriteria the sort criteria chosen
     */
    private void changeSortCriteria(int sortCriteria) {
        sort_criteria = sortCriteria;
        reportsAdapter.sortReports(sortCriteria);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("IMC", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("sort", sortCriteria).apply();
    }

    /** In this class it is defined the style of the dialog shown when user clicks on a report */
    public static class ReportDialog extends DialogFragment {

        /**
         * Static method that returns an instance of ReportDialog
         * with the specified report as an argument
         * @param report the report to be shown
         * @return a ReportDialog instance
         */
        static ReportDialog newInstance(Report report) {
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
            Report report = getArguments().getParcelable("report");
            TextView tv_title = v.findViewById(R.id.tv_title);
            TextView tv_desc = v.findViewById(R.id.tv_desc);
            TextView tv_date = v.findViewById(R.id.tv_date);
            final ImageView iv_image = v.findViewById(R.id.iv_report_image);
            final ProgressBar pb_loading = v.findViewById(R.id.pb_image);
            tv_title.setText(report.getTitle());
            tv_desc.setText(report.getDescription());

            // Time formatting: Created on: date, hour
            DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());
            DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(getContext());
            Date date = new Date(report.getTimestamp());
            String completeDate = dateFormat.format(date) + ", " + timeFormat.format(date);
            tv_date.setText(getString(R.string.report_date, completeDate));
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
    }
}
