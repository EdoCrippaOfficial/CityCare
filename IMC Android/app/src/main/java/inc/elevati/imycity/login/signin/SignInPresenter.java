package inc.elevati.imycity.login.signin;

import inc.elevati.imycity.login.LoginContracts;
import inc.elevati.imycity.utils.EspressoIdlingResource;
import inc.elevati.imycity.utils.MvpContracts;
import inc.elevati.imycity.firebase.FirebaseAuthHelper;

public class SignInPresenter implements LoginContracts.SignInPresenter {

    private LoginContracts.SignInView view;

    private boolean pendingTask;
    private int resultCode;

    @Override
    public void attachView(MvpContracts.MvpView view) {
        this.view = (LoginContracts.SignInView) view;

        // If there were pending tasks, execute them now
        if (pendingTask) {

            // If resultCode is not 0, then onLoginTaskComplete has to be executed
            if (resultCode != 0) {
                onLoginTaskComplete(resultCode);
                resultCode = 0;
            }
            pendingTask = false;
        }

    }

    @Override
    public void detachView() {
        this.view = null;
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

        EspressoIdlingResource.increment();
        view.showProgressDialog();

        FirebaseAuthHelper helper = new FirebaseAuthHelper(this);
        helper.signIn(email, password);
    }

    @Override
    public void onLoginTaskComplete(int resultCode) {

        // If view is detached, set the pendingTask flag
        if (view == null) {
            pendingTask = true;
            this.resultCode = resultCode;
            return;
        }

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
        EspressoIdlingResource.decrement();
    }
}
