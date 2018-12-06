package inc.elevati.imycity.login.signin;

import inc.elevati.imycity.login.LoginContracts;

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
    public void signInButtonClicked(String userName, String password) {
        view.startMainActivity();
    }
}
