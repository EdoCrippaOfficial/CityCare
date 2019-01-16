package inc.elevati.imycity.login;

import inc.elevati.imycity.main.MainActivity;
import inc.elevati.imycity.utils.MvpContracts;

/** Generic interface that defines contracts for login package */
public interface LoginContracts {

    /** Possible results for register task */
    enum RegisterTaskResult {

        /** Register task completed correctly */
        REGISTER_ACCOUNT_CREATED,

        /** Register failed because the email provided is already in the system */
        REGISTER_FAILED_ALREADY_EXISTS,

        /** Register failed due to unknown error */
        REGISTER_FAILED_UNKNOWN
    }

    /** Possible results for login task */
    enum LoginTaskResult {

        /** Login task completed correctly */
        LOGIN_OK,

        /** Login failed because the email provided is not in the system */
        LOGIN_FAILED_NO_ACCOUNT,

        /** Login failed because the password provided is incorrect */
        LOGIN_FAILED_WRONG_PASSWORD,

        /** Login failed due to unknown error */
        LOGIN_FAILED_UNKNOWN,
    }

    /** Implemented by the class that handles general login view (activity) */
    interface LoginView extends MvpContracts.MvpView {

        /**
         * Starts {@link MainActivity}
         */
        void startMainActivity();
    }

    /** Implemented by the class that handles general login presenter */
    interface LoginPresenter extends MvpContracts.MvpPresenter {

        /**
         * Checks if an user is already signed in
         */
        void checkIfAlreadySignedIn();

        /**
         * @return the child {@link RegisterPresenter} instance
         */
        RegisterPresenter getRegisterPresenter();

        /**
         * @return the child {@link SignInPresenter} instance
         */
        SignInPresenter getSignInPresenter();
    }

    /** Implemented by the class that handles sign-in presenter */
    interface SignInPresenter extends MvpContracts.MvpPresenter {

        /** Notifies this presenter that register button was clicked */
        void onRegisterButtonClicked();

        /**
         * Notifies this presenter that sign-in button was clicked
         * @param email the email provided
         * @param password the password provided
         */
        void onSignInButtonClicked(String email, String password);

        /**
         * Notifies this presenter that a login task has completed
         * @param result the task result
         */
        void onSignInTaskComplete(LoginTaskResult result);
    }

    /** Implemented by the class that handles register presenter */
    interface RegisterPresenter extends MvpContracts.MvpPresenter {

        /** Notifies this presenter that sign-in button was clicked */
        void onSignInButtonClicked();

        /**
         * Notifies this presenter that register button was clicked
         * @param name the name provided
         * @param ssn the Social Security Number provided
         * @param email the email provided
         * @param password the password provided
         */
        void onRegisterButtonClicked(String name, String ssn, String email, String password);

        /**
         * Notifies this presenter that a register task has completed
         * @param result the task result
         */
        void onRegisterTaskComplete(RegisterTaskResult result);
    }

    /** Implemented by the class that handles sign-in view (fragment) */
    interface SignInView extends MvpContracts.MvpView {

        /**
         * Changes visible visible view showing the register view
         */
        void switchToRegisterView();

        /**
         * Shows a non-cancelable progress dialog
         */
        void showProgressDialog();

        /**
         * Dismisses the progress dialog if shown
         */
        void dismissProgressDialog();

        /**
         * Starts {@link MainActivity}
         */
        void startMainActivity();

        /**
         * Notifies this view that the email provided is not in the system
         */
        void notifyAccountNotExists();

        /**
         * Notifies this view that the password provided is incorrect
         */
        void notifyWrongPassword();

        /**
         * Notifies this view of an unknown error during sign-in task
         */
        void notifyUnknownError();

        /**
         * Notifies this view that the email provided is not correctly formed
         */
        void notifyInvalidEmail();

        /**
         * Notifies this view that the password provided is not correctly formed
         */
        void notifyInvalidPassword();
    }


    interface RegisterView extends MvpContracts.MvpView {

        /**
         * Changes visible visible view showing the sign-in view
         */
        void switchToSignInView();

        /**
         * Shows a non-cancelable progress dialog
         */
        void showProgressDialog();

        /**
         * Dismisses the progress dialog if shown
         */
        void dismissProgressDialog();

        /**
         * Starts {@link MainActivity}
         */
        void startMainActivity();

        /**
         * Notifies this view that the email provided is already in the system
         */
        void notifyEmailAlreadyExists();

        /**
         * Notifies this view of an unknown error during register task
         */
        void notifyUnknownError();

        /**
         * Notifies this view that the name provided is empty
         */
        void notifyInvalidName();

        /**
         * Notifies this view that the email provided is not correctly formed
         */
        void notifyInvalidEmail();

        /**
         * Notifies this view that the Social Security Number provided is not correctly formed
         */
        void notifyInvalidSSN();

        /**
         * Notifies this view that the password provided is not correctly formed
         */
        void notifyInvalidPassword();
    }
}
