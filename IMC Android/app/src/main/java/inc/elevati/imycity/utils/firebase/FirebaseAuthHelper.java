package inc.elevati.imycity.utils.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import androidx.annotation.NonNull;
import inc.elevati.imycity.login.LoginContracts;

public class FirebaseAuthHelper {

    public static String getUserName() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    public static String getUserEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    public static String getUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static boolean isAuthenticated() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static void signOut() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
    }

    public static void register(String email, String password, final LoginContracts.RegisterPresenter presenter) {

        // Try to create account
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            presenter.onRegisterTaskComplete(LoginContracts.REGISTER_ACCOUNT_CREATED);
                        } else {

                            // Account already exists
                            if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                presenter.onRegisterTaskComplete(LoginContracts.REGISTER_FAILED_ALREADY_EXISTS);

                            // Unknown error
                            else
                                presenter.onRegisterTaskComplete(LoginContracts.REGISTER_FAILED_UNKNOWN);
                        }
                    }
                });
    }

    public static void signIn(String email, String password, final LoginContracts.SignInPresenter presenter) {
        // Try to sign in
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            presenter.onLoginTaskComplete(LoginContracts.LOGIN_OK);
                        } else {

                            // Account doesn't exists
                            if (task.getException() instanceof FirebaseAuthInvalidUserException)
                                presenter.onLoginTaskComplete(LoginContracts.LOGIN_FAILED_NO_ACCOUNT);

                            // Wrong password
                            else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                                presenter.onLoginTaskComplete(LoginContracts.LOGIN_FAILED_WRONG_PASSWORD);

                            // Unknown error
                            else
                                presenter.onLoginTaskComplete(LoginContracts.LOGIN_FAILED_UNKNOWN);
                        }
                    }
                });
    }
}
