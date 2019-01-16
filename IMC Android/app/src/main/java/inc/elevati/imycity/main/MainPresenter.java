package inc.elevati.imycity.main;

import inc.elevati.imycity.R;
import inc.elevati.imycity.main.allreports.AllReportsFragment;
import inc.elevati.imycity.main.allreports.AllReportsPresenter;
import inc.elevati.imycity.main.completedreports.CompletedReportsFragment;
import inc.elevati.imycity.main.completedreports.CompletedReportsPresenter;
import inc.elevati.imycity.main.myreports.MyReportsFragment;
import inc.elevati.imycity.main.myreports.MyReportsPresenter;
import inc.elevati.imycity.main.newreport.NewReportFragment;
import inc.elevati.imycity.main.newreport.NewReportPresenter;
import inc.elevati.imycity.main.starredreports.StarredReportsFragment;
import inc.elevati.imycity.main.starredreports.StarredReportsPresenter;
import inc.elevati.imycity.utils.MvpContracts;
import inc.elevati.imycity.firebase.FirebaseAuthHelper;

/** Presenter associated to {@link MainActivity} */
public class MainPresenter implements MainContracts.MainPresenter {

    /** The view associated to this presenter */
    private MainContracts.MainView view;

    /** The presenter associated to child view {@link NewReportFragment} */
    private MainContracts.NewReportPresenter newReportPresenter;

    /** The presenter associated to child view {@link AllReportsFragment} */
    private MainContracts.ReportListPresenter allReportsPresenter;

    /** The presenter associated to child view {@link MyReportsFragment} */
    private MainContracts.ReportListPresenter myReportsPresenter;

    /** The presenter associated to child view {@link CompletedReportsFragment} */
    private MainContracts.ReportListPresenter completedReportsPresenter;

    /** The presenter associated to child view {@link StarredReportsFragment} */
    private MainContracts.ReportListPresenter starredReportsPresenter;

    /**
     * Constructor where children presenters are instantiated too
     */
    MainPresenter() {
        this.allReportsPresenter = new AllReportsPresenter();
        this.newReportPresenter = new NewReportPresenter();
        this.myReportsPresenter = new MyReportsPresenter();
        this.starredReportsPresenter = new StarredReportsPresenter();
        this.completedReportsPresenter = new CompletedReportsPresenter();
    }

    /** {@inheritDoc} */
    @Override
    public void attachView(MvpContracts.MvpView view) {
        this.view = (MainContracts.MainView) view;
    }

    /** {@inheritDoc} */
    @Override
    public void detachView() {
        this.view = null;
    }

    /** {@inheritDoc} */
    @Override
    public MainContracts.NewReportPresenter getNewReportPresenter() {
        return newReportPresenter;
    }

    /** {@inheritDoc} */
    @Override
    public MainContracts.ReportListPresenter getAllReportsPresenter() {
        return allReportsPresenter;
    }

    /** {@inheritDoc} */
    @Override
    public MainContracts.ReportListPresenter getCompletedReportsPresenter() {
        return completedReportsPresenter;
    }

    /** {@inheritDoc} */
    @Override
    public MainContracts.ReportListPresenter getMyReportsPresenter() {
        return myReportsPresenter;
    }

    /** {@inheritDoc} */
    @Override
    public MainContracts.ReportListPresenter getStarredReportsPresenter() {
        return starredReportsPresenter;
    }

    /** {@inheritDoc} */
    @Override
    public void onMenuItemClicked(int itemId) {
        switch (itemId) {
            case R.id.menu_new:
                view.scrollToPage(MainContracts.PAGE_NEW);
                break;
            case R.id.menu_all:
                view.scrollToPage(MainContracts.PAGE_ALL);
                break;
            case R.id.menu_my:
                view.scrollToPage(MainContracts.PAGE_MY);
                break;
            case R.id.menu_starred:
                view.scrollToPage(MainContracts.PAGE_STARRED);
                break;
            case R.id.menu_completed:
                view.scrollToPage(MainContracts.PAGE_COMPLETED);
                break;
            case R.id.menu_logout:
                FirebaseAuthHelper.signOut();
                view.startLoginActivity();
                break;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onPageScrolled(int page) {
        switch (page) {
            case MainContracts.PAGE_NEW:
                view.setCheckedMenuItem(R.id.menu_new);
                break;
            case MainContracts.PAGE_ALL:
                view.setCheckedMenuItem(R.id.menu_all);
                break;
            case MainContracts.PAGE_MY:
                view.setCheckedMenuItem(R.id.menu_my);
                break;
            case MainContracts.PAGE_STARRED:
                view.setCheckedMenuItem(R.id.menu_starred);
                break;
            case MainContracts.PAGE_COMPLETED:
                view.setCheckedMenuItem(R.id.menu_completed);
                break;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getCurrentUserEmail() {
        return FirebaseAuthHelper.getUserEmail();
    }

    /** {@inheritDoc} */
    @Override
    public String getCurrentUserName() {
        return FirebaseAuthHelper.getUserName();
    }
}
