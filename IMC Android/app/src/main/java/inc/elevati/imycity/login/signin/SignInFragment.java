package inc.elevati.imycity.login.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import inc.elevati.imycity.R;
import inc.elevati.imycity.login.LoginActivity;
import inc.elevati.imycity.login.LoginContracts;
import inc.elevati.imycity.login.register.RegisterFragment;
import inc.elevati.imycity.main.MainActivity;
import inc.elevati.imycity.utils.ProgressDialog;

/** Fragment displayed when the user wants to sign-in */
public class SignInFragment extends Fragment implements LoginContracts.SignInView {

    /** {@link TextInputEditText}s for user data */
    private TextInputEditText textInputEmail, textInputPassword;

    /** {@link TextInputLayout}s for user data */
    private TextInputLayout textLayoutEmail, textLayoutPassword;

    /** The presenter associated to this view */
    private LoginContracts.SignInPresenter presenter;

    /** Dialog displayed during tasks execution */
    private ProgressDialog progressDialog;

    /**
     * Method that has to be used to get a SignInFragment instance instead of constructor
     * @return a new SignInFragment instance
     */
    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    /**
     * Method called when the View associated to this fragment is created (the first time this
     * fragment is shown, at orientation changes, at activity re-creations...); here the layout
     * is inflated and all Views owned by this fragment are initialized.
     * @param inflater the layout inflater
     * @param container this fragment parent view
     * @param savedInstanceState a Bundle containing saved data to be restored
     * @return the View initialized and inflated
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        textInputEmail = v.findViewById(R.id.text_input_edit_email);
        textInputPassword = v.findViewById(R.id.text_input_edit_password);
        textLayoutEmail = v.findViewById(R.id.text_input_layout_email);
        textLayoutPassword = v.findViewById(R.id.text_input_layout_password);

        // Clear error when users provides input in TextInputEditTexts
        clearEditTextErrorOnInput();

        // ProgressDialog retrieval
        progressDialog = (ProgressDialog) getFragmentManager().findFragmentByTag("progress");

        // Presenter retrieval
        presenter = ((LoginActivity) getActivity()).getPresenter().getSignInPresenter();

        // Switch to register fragment
        v.findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               presenter.onRegisterButtonClicked();
            }
        });

        // Login button clicked
        v.findViewById(R.id.bn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInputEmail.clearFocus();
                textInputPassword.clearFocus();
                textLayoutEmail.setError(null);
                textLayoutPassword.setError(null);
                String email = textInputEmail.getText().toString();
                String password = textInputPassword.getText().toString();
                presenter.onSignInButtonClicked(email, password);
            }
        });
        return v;
    }

    /** {@inheritDoc} */
    @Override
    public void switchToRegisterView() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container_login, RegisterFragment.newInstance());
        fragmentTransaction.commit();
    }

    /** {@inheritDoc} */
    @Override
    public void showProgressDialog() {
        progressDialog = ProgressDialog.newInstance(R.string.login_loading);
        progressDialog.show(getFragmentManager(), "progress");
    }

    /** {@inheritDoc} */
    @Override
    public void dismissProgressDialog() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    /** {@inheritDoc} */
    @Override
    public void notifyInvalidEmail() {
        textLayoutEmail.setError(getString(R.string.login_no_email));
    }

    /** {@inheritDoc} */
    @Override
    public void notifyInvalidPassword() {
        textLayoutPassword.setError(getString(R.string.login_no_password));
    }

    /** {@inheritDoc} */
    @Override
    public void startMainActivity() {
        Activity activity = getActivity();
        if (activity == null) return;
        Intent intent = new Intent(activity, MainActivity.class);
        startActivity(intent);
        activity.finish();
    }

    /** {@inheritDoc} */
    @Override
    public void notifyAccountNotExists() {
        if (isAdded()) textLayoutEmail.setError(getString(R.string.login_account_not_exists));
    }

    /** {@inheritDoc} */
    @Override
    public void notifyWrongPassword() {
        if (isAdded()) textLayoutPassword.setError(getString(R.string.login_wrong_password));
    }

    /** {@inheritDoc} */
    @Override
    public void notifyUnknownError() {
        if (isAdded())
            Toast.makeText(getContext(), R.string.login_unknown_error, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method called to hide error when user provides new text in {@link TextInputEditText}s
     */
    private void clearEditTextErrorOnInput() {
        textInputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textLayoutEmail.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        textInputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textLayoutPassword.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}