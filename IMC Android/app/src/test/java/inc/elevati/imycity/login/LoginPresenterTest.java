package inc.elevati.imycity.login;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import inc.elevati.imycity.firebase.FirebaseAuthHelper;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoginPresenter.class, FirebaseAuthHelper.class})
public class LoginPresenterTest {

    @Mock
    private LoginContracts.LoginView view;

    private LoginContracts.LoginPresenter presenter;

    @Before
    public void setUp() {
        view = mock(LoginActivity.class);
        presenter = new LoginPresenter();
        presenter.attachView(view);
    }

    @Test
    public void checkIfAlreadySignedInTest() {
        mockStatic(FirebaseAuthHelper.class);

        // Not authenticated case
        when(FirebaseAuthHelper.isAuthenticated()).thenReturn(false);
        presenter.checkIfAlreadySignedIn();
        verify(view, never()).startMainActivity();

        // Authenticated case
        when(FirebaseAuthHelper.isAuthenticated()).thenReturn(true);
        presenter.checkIfAlreadySignedIn();
        verify(view).startMainActivity();
    }
}
