package inc.elevati.imycity.login;

public interface LoginContracts {

    int REGISTER_ACCOUNT_CREATED = 1;
    int REGISTER_FAILED_ALREADY_EXISTS = 2;
    int REGISTER_FAILED_UNKNOWN = 4;
    int LOGIN_OK = 1;
    int LOGIN_FAILED_NO_ACCOUNT = 2;
    int LOGIN_FAILED_WRONG_PASSWORD = 3;
    int LOGIN_FAILED_UNKNOWN = 4;

    interface SignInPresenter {

        void registerButtonClicked();

        void signInButtonClicked(String email, String password);

        void onLoginTaskComplete(int resultCode);
    }

    interface RegisterPresenter {

        void registerButtonClicked(String name, String ssn, String email, String password);

        void onRegisterTaskComplete(int resultCode);
    }

    interface SignInView {

        void showProgressDialog();

        void dismissProgressDialog();

        void startMainActivity();

        void notifyAccountNotExists();

        void notifyWrongPassword();

        void notifyUnknownError();

        void switchToRegisterView();

        void notifyInvalidEmail();

        void notifyInvalidPassword();
    }


    interface RegisterView {

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
