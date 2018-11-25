package inc.elevati.imycity.AllReportFragment;


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

import java.util.List;

import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.main.all_report_fragment.AllReportsPresenter;
import inc.elevati.imycity.utils.Report;
import inc.elevati.imycity.utils.firebase.FirestoreReader;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
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
    public void LoadReportsTest() throws Exception {
        FirestoreReader firestoreReader = PowerMockito.mock(FirestoreReader.class);
        PowerMockito.whenNew(FirestoreReader.class).withAnyArguments().thenReturn(firestoreReader);
        presenter.loadAllReports();
        verify(firestoreReader).readAllReports();
    }

    @Test
    public void DisplaySnapshotTest() throws Exception {
        presenter.displayAllReports(any(QuerySnapshot.class));
        verify(view).updateReports((List<Report>) any());
    }

    @Test
    public void ResteViewTest(){
        presenter.resetViewRefreshing();
        verify(view).resetRefreshing();
    }

    @Test
    public void ShowReportTest(){
        Report report = Mockito.mock(Report.class);
        presenter.showReport(report);
        verify(view).showReportDialog(report);
    }




}