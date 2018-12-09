package inc.elevati.imycity.login.register;

import java.util.regex.Pattern;

import inc.elevati.imycity.login.LoginContracts;
import inc.elevati.imycity.utils.firebase.FirebaseAuthHelper;

public class RegisterPresenter implements LoginContracts.RegisterPresenter {

    private final static String ssnRegex = "^(?:(?:[B-DF-HJ-NP-TV-Z]|[AEIOU])[AEIOU][AEIOUX]|[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[1256LMRS][\\dLMNP-V])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM])(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))[A-Z]$";
    private final static String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";
    private final static String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    private LoginContracts.RegisterView view;

    public RegisterPresenter(LoginContracts.RegisterView view) {
        this.view = view;
    }

    @Override
    public void registerButtonClicked(String name, String ssn, String email, String password) {

        // Name check
        if (name.equals("")) {
            view.notifyInvalidName();
            return;
        }

        // SSN check
        if (!Pattern.matches(ssnRegex, ssn)) {
            view.notifyInvalidSSN();
            return;
        }

        // Email check
        if (!Pattern.matches(emailRegex, email)) {
            view.notifyInvalidEmail();
            return;
        }

        // Password check
        if (!Pattern.matches(passwordRegex, password)) {
            view.notifyInvalidPassword();
            return;
        }

        // Everything is ok, proceed with register process
        view.showProgressDialog();
        FirebaseAuthHelper.register(email, password, this);
    }

    @Override
    public void onRegisterTaskComplete(int resultCode) {
        if (resultCode == LoginContracts.REGISTER_ACCOUNT_CREATED) {
            view.startMainActivity();
        } else if (resultCode == LoginContracts.REGISTER_FAILED_ALREADY_EXISTS){
            view.notifyEmailAlreadyExists();
        } else {
            view.notifyUnknownError();
        }
        view.dismissProgressDialog();
    }
}
