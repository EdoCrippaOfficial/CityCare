package inc.elevati.imycity.NewReportFragment;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.main.new_report_fragment.NewReportPresenter;
import inc.elevati.imycity.utils.Report;
import inc.elevati.imycity.utils.firebase.FirestoreSender;
import inc.elevati.imycity.utils.firebase.StorageWriter;

import static org.mockito.Mockito.verify;

//  annotazioni per PowerMockito
@RunWith(PowerMockRunner.class)
@PrepareForTest(NewReportPresenter.class)

public class NewReportPresenterTest {

    @Mock
    private MainContracts.NewReportView view;   // mock della view associata

    @Mock
    private Bitmap image;      // mock bitmap

    @Mock
    private Report report;     // mock report

    private NewReportPresenter presenter;

    @Before
    public void setUp() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        presenter = new NewReportPresenter(view);
    }

    @Test
    public void testHandling() throws Exception {

        //  riferimento all'oggetto StorageWriter creato
        StorageWriter storageWriter = PowerMockito.mock(StorageWriter.class);
        PowerMockito.whenNew(StorageWriter.class).withAnyArguments().thenReturn(storageWriter);

        //  riferimento all'oggetto Report creato
        Report report = PowerMockito.mock(Report.class);
        PowerMockito.whenNew(Report.class).withAnyArguments().thenReturn(report);

        //  chiamo la funzione
        presenter.handleSendReport(image, "TitleTest", "DesctiptionTest");

        //  verifico che il nuovo oggetto StorageWriter usi il metodo send con il report creato
        verify(storageWriter).send(image, report);

    }

    @Test
    public void FirestoreSenderTest() throws Exception {
        FirestoreSender firestoreSender = PowerMockito.mock(FirestoreSender.class);
        PowerMockito.whenNew(FirestoreSender.class).withAnyArguments().thenReturn(firestoreSender);

        presenter.sendReportData(report);
        verify(firestoreSender).send(report);

    }

    @Test
    public void dismissTest(){

        //  chiamo il metodo
        presenter.dismissViewDialog(false);

        //  verifico la chiamata al metodo della view
        verify(view).dismissProgressDialog(false);
    }


}
