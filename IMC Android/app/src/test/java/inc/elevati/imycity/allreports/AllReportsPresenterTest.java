package inc.elevati.imycity.allreports;

import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import inc.elevati.imycity.firebase.FirestoreHelper;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.main.allreports.AllReportsPresenter;
import inc.elevati.imycity.utils.Report;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AllReportsPresenter.class)

public class AllReportsPresenterTest {

    @Mock
    private MainContracts.ReportsView view;

    private AllReportsPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new AllReportsPresenter();
    }

    @Test
    public void loadReportsTest() {
        mockStatic(FirestoreHelper.class);
        presenter.loadReports();
        verifyStatic();
        FirestoreHelper.readAllReports(presenter);
    }

    @Test
    public void displaySnapshotTest() {
        presenter.onLoadReportsTaskComplete(any(QuerySnapshot.class));
        verify(view).updateReports(anyListOf(Report.class));
    }

    @Test
    public void resetRefreshTest(){
        presenter.onUpdateTaskComplete();
        verify(view).resetRefreshing();
    }

    @Test
    public void showReportTest(){
        Report report = Mockito.mock(Report.class);
        presenter.showReport(report);
        verify(view).showReportDialog(report);
    }
}