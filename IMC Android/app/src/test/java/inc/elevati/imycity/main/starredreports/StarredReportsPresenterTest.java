package inc.elevati.imycity.main.starredreports;

import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import inc.elevati.imycity.firebase.FirebaseAuthHelper;
import inc.elevati.imycity.firebase.FirestoreHelper;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.main.ReportDialog;
import inc.elevati.imycity.utils.Report;

import static inc.elevati.imycity.main.MainContracts.DeleteReportTaskResult.RESULT_FAILED;
import static inc.elevati.imycity.main.MainContracts.DeleteReportTaskResult.RESULT_OK;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({StarredReportsPresenter.class, FirebaseAuthHelper.class})
public class StarredReportsPresenterTest {

    @Mock
    private MainContracts.ReportListView view;

    @Mock
    private FirestoreHelper firestoreHelper;

    private String uid;

    private StarredReportsPresenter presenter;

    @Before
    public void setUp() throws Exception {
        view = mock(StarredReportsFragment.class);
        presenter = new StarredReportsPresenter();
        presenter.attachView(view);
        firestoreHelper = mock(FirestoreHelper.class);
        whenNew(FirestoreHelper.class).withArguments(presenter).thenReturn(firestoreHelper);

        // Mock this static method used inside the tested code
        uid = "dummy";
        mockStatic(FirebaseAuthHelper.class);
        when(FirebaseAuthHelper.getUserId()).thenReturn(uid);
    }

    @Test
    public void loadReportsTest() {
        presenter.loadReports();
        verify(firestoreHelper).readStarredReports(uid);
    }

    @Test
    public void onLoadReportsTaskCompleteTest() {
        presenter.onLoadReportsTaskComplete(any(QuerySnapshot.class));
        verify(view).updateReports(anyListOf(Report.class));
    }

    @Test
    public void onUpdateTaskCompleteTest(){
        presenter.onUpdateTaskComplete();
        verify(view).resetRefreshing();
    }

    @Test
    public void showReportTest(){
        Report report = mock(Report.class);
        presenter.onReportClicked(report);
        verify(view).showReportDialog(report);
    }

    @Test
    public void starsButtonClickedTest() {

        // Not mocked because we want to test the true and false cases of "starred" field
        Report report = new Report(null, null, null, 0, null, null, null);
        report.setStarred(true);
        presenter.onStarsButtonClicked(report);

        // Check if unstarReport method is called, as report was starred
        verify(firestoreHelper).unstarReport(report, uid);
        report.setStarred(false);
        presenter.onStarsButtonClicked(report);

        // Check if starReport method is called, as report was not starred
        verify(firestoreHelper).starReport(report, uid);
    }

    @Test
    public void onStarOperationCompleteTest() {
        presenter.onStarTaskComplete();
        verify(firestoreHelper).readStarredReports(uid);
    }

    @Test
    public void onDeleteReportButtonClickedTest() {

        // Create a mock ReportDialogView
        MainContracts.ReportDialogView dialogView = mock(ReportDialog.class);
        presenter.attachReportDialogView(dialogView);
        Report report = mock(Report.class);
        presenter.onDeleteReportButtonClicked(report);

        // Verify methods call
        verify(dialogView).showProgressDialog();
        verify(firestoreHelper).deleteReport(report.getId());
    }

    @Test
    public void onDeleteReportTaskCompleteTest() {

        // Create a mock ReportDialogView
        MainContracts.ReportDialogView dialogView = mock(ReportDialog.class);
        presenter.attachReportDialogView(dialogView);

        // Set result to RESULT_OK
        presenter.onDeleteReportTaskComplete(RESULT_OK);

        // Verify methods calls
        verify(dialogView).dismissDialog();

        // Set result to RESULT_FAILED
        presenter.onDeleteReportTaskComplete(RESULT_FAILED);

        // Verify methods calls
        verify(dialogView, times(2)).dismissProgressDialog();
        verify(dialogView).notifyDeleteReportError();
    }
}