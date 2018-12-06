package inc.elevati.imycity.login;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import inc.elevati.imycity.R;
import inc.elevati.imycity.login.signin.SignInFragment;
import inc.elevati.imycity.main.MainActivity;

public class LoginActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_login, SignInFragment.newInstance());
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if the user is already signed in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            // Go to main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
