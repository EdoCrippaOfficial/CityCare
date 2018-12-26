package inc.elevati.imycity.allreports;

import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.main.allreports.AllReportsPresenter;
import inc.elevati.imycity.utils.Report;
import inc.elevati.imycity.firebase.FirestoreReader;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AllReportsPresenter.class)

public class AllReportsPresenterTest {

    @Mock
    private MainContracts.AllReportsView view;

    private AllReportsPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new AllReportsPresenter(view);
    }

    @Test
    public void loadReportsTest() throws Exception {
        FirestoreReader firestoreReader = PowerMockito.mock(FirestoreReader.class);
        PowerMockito.whenNew(FirestoreReader.class).withAnyArguments().thenReturn(firestoreReader);
        presenter.loadAllReports();
        verify(firestoreReader).readAllReports();
    }

    @Test
    public void displaySnapshotTest() {
        presenter.onLoadAllReportsTaskComplete(any(QuerySnapshot.class));
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