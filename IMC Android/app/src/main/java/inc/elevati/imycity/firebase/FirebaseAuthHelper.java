package inc.elevati.imycity.firebase;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;
import inc.elevati.imycity.login.LoginContracts;

public class FirebaseAuthHelper {

    // Current user information
    private static String userName;
    private static String userEmail;
    private static String userId;

    public static String getUserName() {
        if (userName == null)
            userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        return userName;
    }

    public static String getUserEmail() {
        if (userEmail == null)
            userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        return userEmail;
    }

    public static String getUserId() {
        if (userId == null)
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return userId;
    }

    public static boolean isAuthenticated() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static void signOut() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
    }

    public static void register(final String name, String email, String password, final LoginContracts.RegisterPresenter presenter) {

        // Try to create account
        Task<Void> mainTask = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)

                // Update user name
                .continueWithTask(new Continuation<AuthResult, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<AuthResult> t) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        return t.getResult().getUser().updateProfile(profileUpdates);
                    }
                });

        // Result listener
        mainTask.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
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

                            // On sign in set current user information
                            userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                            userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
