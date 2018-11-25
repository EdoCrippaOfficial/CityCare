package inc.elevati.imycity.newreport;

import android.content.Context;
import android.net.Uri;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import inc.elevati.imycity.R;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.main.newreport.NewReportFragment;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)

public class NewReportViewTest {

    private NewReportFragment fragment = new NewReportFragment();

    @Before
    public void setup(){
        SupportFragmentTestUtil.startVisibleFragment(fragment);
    }

    @Test
    public void sendButtonTest(){

        //  Riferimento al bottone per l'invio
        Button sendBn = fragment.getActivity().findViewById(R.id.bn_new_report_send);

        //  Settiamo come presenter non quello vero ma un mock
        fragment.presenter = mock(MainContracts.NewReportPresenter.class);

        sendBn.performClick();
        verify(fragment.presenter).sendButtonClicked(any(String.class), any(String.class), any(Context.class), any(Uri.class));
    }
}