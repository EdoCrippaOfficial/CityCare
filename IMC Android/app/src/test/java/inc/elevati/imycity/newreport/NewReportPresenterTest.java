package inc.elevati.imycity.newreport;

import android.content.Context;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import inc.elevati.imycity.firebase.StorageHelper;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.main.newreport.NewReportPresenter;
import inc.elevati.imycity.utils.Report;

import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(NewReportPresenter.class)

public class NewReportPresenterTest {

    @Mock
    private MainContracts.NewReportView view;   // mock della view associata

    private NewReportPresenter presenter;

    @Before
    public void setUp() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        presenter = new NewReportPresenter();
    }

    @Test
    public void sendButtonTest() throws Exception {

       /* //  riferimento all'oggetto StorageHelper creato
        StorageHelper storageWriter = PowerMockito.mock(StorageHelper.class);
        PowerMockito.whenNew(StorageHelper.class).withAnyArguments().thenReturn(storageWriter);

        //  riferimento all'oggetto Report creato
        Report report = PowerMockito.mock(Report.class);
        PowerMockito.whenNew(Report.class).withAnyArguments().thenReturn(report);

        // Mocks
        Context appContext = Mockito.mock(Context.class);
        Uri imageUri = Mockito.mock(Uri.class);

        //  chiamo la funzione con "null" come Uri quindi senza immagine
        presenter.sendButtonClicked("", "", appContext, null);
        verify(view).notifyInvalidImage();

        //  chiamo la funzione con con immagine ma senza un titolo
        presenter.sendButtonClicked("", "", appContext, imageUri);
        verify(view).notifyInvalidTitle();

        //  chiamo la funzione senza descrizione
        presenter.sendButtonClicked("Titolo", "", appContext, imageUri);
        verify(view).notifyInvalidDescription();

        //  chiamo correttamente la funzione
        presenter.sendButtonClicked("Titolo", "Descrizione", appContext, imageUri);

        //  verifico che il nuovo oggetto StorageHelper usi il metodo sendReport con il report creato
        verify(storageWriter).send(report, appContext, imageUri);*/
    }

    @Test
    public void firestoreSenderTest() throws Exception {
        /*FirestoreSender firestoreSender = PowerMockito.mock(FirestoreSender.class);
        PowerMockito.whenNew(FirestoreSender.class).withAnyArguments().thenReturn(firestoreSender);

        Report report = Mockito.mock(Report.class);
        presenter.sendReportData(report);
        verify(firestoreSender).send(report);*/
    }

    @Test
    public void dismissDialogTest(){
        presenter.onSendTaskComplete(MainContracts.RESULT_SEND_OK);
        verify(view).dismissProgressDialog();
        verify(view).notifySendTaskCompleted(MainContracts.RESULT_SEND_OK);
    }
}
