package inc.elevati.imycity.login;

import inc.elevati.imycity.login.register.RegisterPresenter;
import inc.elevati.imycity.login.signin.SignInPresenter;
import inc.elevati.imycity.utils.MvpContracts;
import inc.elevati.imycity.firebase.FirebaseAuthHelper;

public class LoginPresenter implements LoginContracts.LoginPresenter {

    private LoginContracts.LoginView view;

    private LoginContracts.RegisterPresenter registerPresenter;
    private LoginContracts.SignInPresenter signInPresenter;

    public LoginPresenter() {
        registerPresenter = new RegisterPresenter();
        signInPresenter = new SignInPresenter();
    }

    @Override
    public void attachView(MvpContracts.MvpView view) {
        this.view = (LoginContracts.LoginView) view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public LoginContracts.RegisterPresenter getRegisterPresenter() {
        return registerPresenter;
    }

    public LoginContracts.SignInPresenter getSignInPresenter() {
        return signInPresenter;
    }

    @Override
    public void checkIfAlreadySignedIn() {
        if (FirebaseAuthHelper.isAuthenticated()) view.startMainActivity();
    }
}
