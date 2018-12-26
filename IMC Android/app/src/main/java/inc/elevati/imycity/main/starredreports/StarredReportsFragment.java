package inc.elevati.imycity.main.starredreports;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import inc.elevati.imycity.R;
import inc.elevati.imycity.main.MainActivity;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.main.ReportDialog;
import inc.elevati.imycity.main.ReportsAdapter;
import inc.elevati.imycity.utils.Report;

import static inc.elevati.imycity.main.MainContracts.REPORT_SORT_DATE_NEWEST;
import static inc.elevati.imycity.main.MainContracts.REPORT_SORT_DATE_OLDEST;
import static inc.elevati.imycity.main.MainContracts.REPORT_SORT_STARS_LESS;
import static inc.elevati.imycity.main.MainContracts.REPORT_SORT_STARS_MORE;

public class StarredReportsFragment extends Fragment implements MainContracts.ReportsView, SwipeRefreshLayout.OnRefreshListener {

    /** The sorting criteria chosen */
    private static int sort_criteria;

    /** The RecyclerView adapter */
    private ReportsAdapter reportsAdapter;

    /** Presenter that handles non graphics-related requests */
    private MainContracts.ReportListPresenter presenter;

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
        View v = inflater.inflate(R.layout.fragment_my_reports, container, false);
        setHasOptionsMenu(true);

        // Swipe refresh
        refresher = v.findViewById(R.id.refresher);
        refresher.setOnRefreshListener(this);
        refresher.setColorSchemeResources(R.color.color_primary);

        // Presenter retrieval
        presenter = ((MainActivity) getActivity()).getPresenter().getStarredReportsPresenter();
        reportsAdapter = new ReportsAdapter(this, presenter);

        // Shared preferences for report sorting criteria
        if (savedInstanceState == null) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("IMC", Context.MODE_PRIVATE);
            sort_criteria = sharedPreferences.getInt("sort", REPORT_SORT_DATE_NEWEST);
        }

        // RecyclerView
        RecyclerView recyclerView = v.findViewById(R.id.recycler_my_reports);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(reportsAdapter);

        // Load my reports
        refresher.setRefreshing(true);
        presenter.loadReports();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    /**
     * Static method that returns an instance of this fragment
     * @return a NewReportFragment instance
     */
    public static StarredReportsFragment newInstance() {
        return new StarredReportsFragment();
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
        ReportDialog dialog = ReportDialog.newInstance(report, presenter);
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
        presenter.loadReports();
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
                    case REPORT_SORT_DATE_NEWEST:
                        idChecked = R.id.bn_newest;
                        break;
                    case REPORT_SORT_DATE_OLDEST:
                        idChecked = R.id.bn_oldest;
                        break;
                    case REPORT_SORT_STARS_MORE:
                        idChecked = R.id.bn_more_stars;
                        break;
                    case REPORT_SORT_STARS_LESS:
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
}
