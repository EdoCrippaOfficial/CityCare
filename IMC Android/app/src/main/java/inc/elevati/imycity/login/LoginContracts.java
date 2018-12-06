package inc.elevati.imycity.login;

public interface LoginContracts {

    int REGISTER_ACCOUNT_CREATED = 1;
    int REGISTER_FAILED = 2;

    interface SignInPresenter {

        void registerButtonClicked();

        void signInButtonClicked(String userName, String password);
    }

    interface SignInView {

        void switchToRegisterView();

        void startMainActivity();
    }

    interface RegisterPresenter {

        void registerButtonClicked(String name, String ssn, String email, String password);

        void onRegisterTaskComplete(int resultCode);
    }

    interface RegisterView {

        void startMainActivity();

        void notifyInvalidName();

        void notifyInvalidEmail();

        void notifyInvalidSSN();

        void notifyInvalidPassword();
    }
}
