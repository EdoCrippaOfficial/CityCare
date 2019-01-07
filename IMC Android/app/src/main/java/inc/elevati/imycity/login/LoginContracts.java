package inc.elevati.imycity.login;

import inc.elevati.imycity.utils.MvpContracts;

public interface LoginContracts {

    int REGISTER_ACCOUNT_CREATED = 1;
    int REGISTER_FAILED_ALREADY_EXISTS = 2;
    int REGISTER_FAILED_UNKNOWN = 3;
    int LOGIN_OK = 1;
    int LOGIN_FAILED_NO_ACCOUNT = 2;
    int LOGIN_FAILED_WRONG_PASSWORD = 3;
    int LOGIN_FAILED_UNKNOWN = 4;

    interface LoginView extends MvpContracts.MvpView {

        void startMainActivity();
    }

    interface LoginPresenter extends MvpContracts.MvpPresenter {

        void checkIfAlreadySignedIn();

        SignInPresenter getSignInPresenter();

        RegisterPresenter getRegisterPresenter();
    }

    interface SignInPresenter extends MvpContracts.MvpPresenter {

        void registerButtonClicked();

        void signInButtonClicked(String email, String password);

        void onLoginTaskComplete(int resultCode);
    }

    interface RegisterPresenter extends MvpContracts.MvpPresenter {

        void signInButtonClicked();

        void registerButtonClicked(String name, String ssn, String email, String password);

        void onRegisterTaskComplete(int resultCode);
    }

    interface SignInView extends MvpContracts.MvpView {

        void switchToRegisterView();

        void showProgressDialog();

        void dismissProgressDialog();

        void startMainActivity();

        void notifyAccountNotExists();

        void notifyWrongPassword();

        void notifyUnknownError();

        void notifyInvalidEmail();

        void notifyInvalidPassword();
    }


    interface RegisterView extends MvpContracts.MvpView {

        void switchToSignInView();

        void showProgressDialog();

        void dismissProgressDialog();

        void startMainActivity();

        void notifyEmailAlreadyExists();

        void notifyUnknownError();

        void notifyInvalidName();

        void notifyInvalidEmail();

        void notifyInvalidSSN();

        void notifyInvalidPassword();
    }
}
