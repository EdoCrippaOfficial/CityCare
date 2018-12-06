package inc.elevati.imycity.login.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import inc.elevati.imycity.R;
import inc.elevati.imycity.login.LoginContracts;
import inc.elevati.imycity.login.register.RegisterFragment;
import inc.elevati.imycity.main.MainActivity;

public class SignInFragment extends Fragment implements LoginContracts.SignInView {

    private LoginContracts.SignInPresenter presenter;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);

        presenter = new SignInPresenter(this);

        v.findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               presenter.registerButtonClicked();
            }
        });

        v.findViewById(R.id.bn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.signInButtonClicked(null, null);
            }
        });
        return v;
    }

    @Override
    public void switchToRegisterView() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container_login, RegisterFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    public void startMainActivity() {
        Activity activity = getActivity();
        if (activity == null) return;
        Intent intent = new Intent(activity, MainActivity.class);
        startActivity(intent);
        activity.finish();
    }
}
