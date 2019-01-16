package inc.elevati.imycity.main.newreport;

import android.content.Context;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import inc.elevati.imycity.firebase.FirebaseAuthHelper;
import inc.elevati.imycity.firebase.FirestoreHelper;
import inc.elevati.imycity.firebase.StorageHelper;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.Report;

import static inc.elevati.imycity.main.MainContracts.SendTaskResult.RESULT_SEND_OK;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NewReportPresenter.class, FirebaseAuthHelper.class})
public class NewReportPresenterTest {

    @Mock
    private MainContracts.NewReportView view;

    private NewReportPresenter presenter;

    @Before
    public void setUp() {
        view = mock(NewReportFragment.class);
        presenter = new NewReportPresenter();
        presenter.attachView(view);
    }

    @Test
    public void sendButtonClickedTest() throws Exception {
        StorageHelper helper = mock(StorageHelper.class);
        whenNew(StorageHelper.class).withArguments(presenter).thenReturn(helper);
        Report report = mock(Report.class);
        whenNew(Report.class).withAnyArguments().thenReturn(report);
        Context appContext = mock(Context.class);
        Uri imageUri = mock(Uri.class);

        // Mock this static method used inside the tested code
        mockStatic(FirebaseAuthHelper.class);
        when(FirebaseAuthHelper.getUserName()).thenReturn(null);

        // Call the function to test with no image
        presenter.onSendButtonClicked("", "", appContext, null, null);
        verify(view).notifyInvalidImage();

        // Call the function with image and no title
        presenter.onSendButtonClicked("", "", appContext, imageUri, null);
        verify(view).notifyInvalidTitle();

        // Call the function with image, title and no description
        presenter.onSendButtonClicked("Title", "", appContext, imageUri, null);
        verify(view).notifyInvalidDescription();

        // Call the function correctly (note that position is optional)
        presenter.onSendButtonClicked("Title", "Description", appContext, imageUri, null);
        verify(view).showProgressDialog();
        verify(helper).sendImage(report, appContext, imageUri);
    }

    @Test
    public void sendReportDataTest() throws Exception {
        FirestoreHelper helper = PowerMockito.mock(FirestoreHelper.class);
        whenNew(FirestoreHelper.class).withArguments(presenter).thenReturn(helper);
        Report report = mock(Report.class);
        presenter.sendReportData(report);
        verify(helper).sendReport(report);
    }

    @Test
    public void onSendTaskCompleteTest() {
        MainContracts.SendTaskResult resultCode = RESULT_SEND_OK;
        presenter.onSendTaskComplete(resultCode);
        verify(view).dismissProgressDialog();
        verify(view).notifySendTaskCompleted(resultCode);
    }
}
