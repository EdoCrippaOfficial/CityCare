package inc.elevati.imycity.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import inc.elevati.imycity.R;
import inc.elevati.imycity.login.signin.SignInFragment;
import inc.elevati.imycity.main.MainActivity;

/** Activity displayed at app launch, here the user can sign-up or sign-in the system */
public class LoginActivity extends FragmentActivity implements LoginContracts.LoginView {

    /** The presenter associated to this view */
    private LoginContracts.LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Presenter creation
        presenter = (LoginPresenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) presenter = new LoginPresenter();
        presenter.attachView(this);

        // Fragment inflation if activity il launched for the first time
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_login, SignInFragment.newInstance());
            fragmentTransaction.commit();
        }
    }

    /**
     * Called at orientation changes or activity re-creations, it retains the presenter
     * @return the presenter associated to the view
     */
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    /**
     * @return the presenter associated to this view
     */
    public LoginContracts.LoginPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.checkIfAlreadySignedIn();
    }

    /** {@inheritDoc} */
    @Override
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
