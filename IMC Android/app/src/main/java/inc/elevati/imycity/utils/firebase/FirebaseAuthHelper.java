package inc.elevati.imycity.utils.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import inc.elevati.imycity.login.LoginContracts;
import inc.elevati.imycity.utils.UtilsContracts;

public class FirebaseAuthHelper implements UtilsContracts.AuthHelper {

    private LoginContracts.RegisterPresenter presenter;

    public FirebaseAuthHelper(LoginContracts.RegisterPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void register(String name, String ssn, String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            presenter.onRegisterTaskComplete(LoginContracts.REGISTER_ACCOUNT_CREATED);
                        } else {
                            task.getException().printStackTrace();
                            presenter.onRegisterTaskComplete(LoginContracts.REGISTER_FAILED);
                        }
                    }
                });
    }
}
