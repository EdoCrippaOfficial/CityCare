package inc.elevati.imycity.main;

import inc.elevati.imycity.R;
import inc.elevati.imycity.main.allreports.AllReportsPresenter;
import inc.elevati.imycity.main.completedreports.CompletedReportsPresenter;
import inc.elevati.imycity.main.myreports.MyReportsPresenter;
import inc.elevati.imycity.main.newreport.NewReportPresenter;
import inc.elevati.imycity.utils.MvpContracts;
import inc.elevati.imycity.utils.firebase.FirebaseAuthHelper;

public class MainPresenter implements MainContracts.MainPresenter {

    private MainContracts.MainView view;

    private MainContracts.NewReportPresenter newReportPresenter;
    private MainContracts.AllReportsPresenter allReportsPresenter;
    private MainContracts.MyReportsPresenter myReportsPresenter;
    private MainContracts.CompletedReportsPresenter completedReportsPresenter;

    public MainPresenter() {
        this.allReportsPresenter = new AllReportsPresenter();
        this.newReportPresenter = new NewReportPresenter();
        this.myReportsPresenter = new MyReportsPresenter();
        this.completedReportsPresenter = new CompletedReportsPresenter();
    }

    @Override
    public void attachView(MvpContracts.MvpView view) {
        this.view = (MainContracts.MainView) view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public MainContracts.NewReportPresenter getNewReportPresenter() {
        return newReportPresenter;
    }

    public MainContracts.AllReportsPresenter getAllReportsPresenter() {
        return allReportsPresenter;
    }

    public MainContracts.CompletedReportsPresenter getCompletedReportsPresenter() {
        return completedReportsPresenter;
    }

    public MainContracts.MyReportsPresenter getMyReportsPresenter() {
        return myReportsPresenter;
    }

    @Override
    public void menuItemClicked(int itemId) {
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
            case R.id.menu_completed:
                view.scrollToPage(MainContracts.PAGE_COMPL);
                break;
            case R.id.menu_logout:
                FirebaseAuthHelper.signOut();
                view.startLoginActivity();
                break;
        }
    }

    @Override
    public void pageScrolled(int page) {
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
            case MainContracts.PAGE_COMPL:
                view.setCheckedMenuItem(R.id.menu_completed);
                break;
        }
    }

    @Override
    public String getCurrentUserEmail() {
        return FirebaseAuthHelper.getUserEmail();
    }

    @Override
    public String getCurrentUserName() {
        return FirebaseAuthHelper.getUserName();
    }
}
