package inc.elevati.imycity.login.signin;

import inc.elevati.imycity.login.LoginContracts;
import inc.elevati.imycity.utils.firebase.FirebaseAuthHelper;

public class SignInPresenter implements LoginContracts.SignInPresenter {

    private LoginContracts.SignInView view;

    SignInPresenter(LoginContracts.SignInView view) {
        this.view = view;
    }

    @Override
    public void registerButtonClicked() {
        view.switchToRegisterView();
    }

    @Override
    public void signInButtonClicked(String email, String password) {
        if (email.equals("")) {
            view.notifyInvalidEmail();
            return;
        } else if (password.equals("")) {
            view.notifyInvalidPassword();
            return;
        }

        view.showProgressDialog();
        FirebaseAuthHelper.signIn(email, password, this);
    }

    @Override
    public void onLoginTaskComplete(int resultCode) {
        if (resultCode == LoginContracts.LOGIN_OK) {
            view.startMainActivity();
        } else if (resultCode == LoginContracts.LOGIN_FAILED_NO_ACCOUNT) {
            view.notifyAccountNotExists();
        } else if (resultCode == LoginContracts.LOGIN_FAILED_WRONG_PASSWORD) {
            view.notifyWrongPassword();
        } else {
            view.notifyUnknownError();
        }
        view.dismissProgressDialog();
    }
}
