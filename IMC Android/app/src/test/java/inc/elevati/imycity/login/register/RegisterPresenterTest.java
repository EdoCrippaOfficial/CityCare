package inc.elevati.imycity.login.register;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import inc.elevati.imycity.firebase.FirebaseAuthHelper;
import inc.elevati.imycity.login.LoginContracts;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RegisterPresenter.class, FirebaseAuthHelper.class})
public class RegisterPresenterTest {

    @Mock
    private LoginContracts.RegisterView view;

    private RegisterPresenter presenter;

    @Before
    public void setUp() {
        view = mock(RegisterFragment.class);
        presenter = new RegisterPresenter();
        presenter.attachView(view);
    }

    @Test
    public void signInButtonClickedTest() {
        presenter.signInButtonClicked();
        verify(view).switchToSignInView();
    }

    @Test
    public void registerButtonClicked() throws Exception {
        FirebaseAuthHelper helper = mock(FirebaseAuthHelper.class);
        whenNew(FirebaseAuthHelper.class).withArguments(presenter).thenReturn(helper);

        String name = "Name";
        String invalidSSN = "PP3PPP33C03L378Q";
        String correctSSN = "PPPPPP33C03L378Q";
        String invalidEmail = "email@provider";
        String correctEmail = "email@provider.domain";
        String invalidPassword = "password";
        String correctPassword = "passw0rd";

        // Call the function to test with no name
        presenter.registerButtonClicked("", null, null, null);
        verify(view).notifyInvalidName();

        // Call the function with invalid SSN
        presenter.registerButtonClicked(name, invalidSSN, null, null);
        verify(view).notifyInvalidSSN();

        // Call the function with invalid email
        presenter.registerButtonClicked(name, correctSSN, invalidEmail, null);
        verify(view).notifyInvalidSSN();

        // Call the function with invalid password
        presenter.registerButtonClicked(name, correctSSN, correctEmail, invalidPassword);
        verify(view).notifyInvalidSSN();

        // Call the function with correct parameters
        presenter.registerButtonClicked(name, correctSSN, correctEmail, correctPassword);
        verify(view).showProgressDialog();
        verify(helper).register(name, correctEmail, correctPassword);
    }

    @Test
    public void onRegisterTaskCompleteTest() {

        // Call the function with REGISTER_ACCOUNT_CREATED as parameter
        presenter.onRegisterTaskComplete(LoginContracts.REGISTER_ACCOUNT_CREATED);
        verify(view).startMainActivity();

        // Call the function with REGISTER_FAILED_ALREADY_EXISTS as parameter
        presenter.onRegisterTaskComplete(LoginContracts.REGISTER_FAILED_ALREADY_EXISTS);
        verify(view).notifyEmailAlreadyExists();

        // Call the function with REGISTER_FAILED_UNKNOWN as parameter
        presenter.onRegisterTaskComplete(LoginContracts.REGISTER_FAILED_UNKNOWN);
        verify(view).notifyUnknownError();

        verify(view, times(3)).dismissProgressDialog();
    }
}
