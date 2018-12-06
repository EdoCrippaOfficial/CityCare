package inc.elevati.imycity.login.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import inc.elevati.imycity.R;
import inc.elevati.imycity.login.LoginContracts;
import inc.elevati.imycity.main.MainActivity;

public class RegisterFragment extends Fragment implements LoginContracts.RegisterView {

    private TextInputEditText textInputName, textInputSSN, textInputEmail, textInputPassword;
    private TextInputLayout textLayoutName, textLayoutSSN, textLayoutEmail, textLayoutPassword;
    private LoginContracts.RegisterPresenter presenter;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RegisterPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        textInputName = v.findViewById(R.id.text_input_edit_name);
        textInputSSN = v.findViewById(R.id.text_input_edit_ssn);
        textInputEmail = v.findViewById(R.id.text_input_edit_email);
        textInputPassword = v.findViewById(R.id.text_input_edit_password);
        textLayoutName = v.findViewById(R.id.text_input_layout_name);
        textLayoutSSN = v.findViewById(R.id.text_input_layout_ssn);
        textLayoutEmail = v.findViewById(R.id.text_input_layout_email);
        textLayoutPassword = v.findViewById(R.id.text_input_layout_password);

        resetErrorOnTextInput();

        v.findViewById(R.id.bn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = textInputName.getText().toString();
                String ssn = textInputSSN.getText().toString();
                String email = textInputEmail.getText().toString();
                String password = textInputPassword.getText().toString();
                presenter.registerButtonClicked(name, ssn, email, password);
            }
        });

        v.findViewById(R.id.tv_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        return v;
    }

    @Override
    public void startMainActivity() {
        Activity activity = getActivity();
        if (activity == null) return;
        Intent intent = new Intent(activity, MainActivity.class);
        startActivity(intent);
        activity.finish();
    }

    @Override
    public void notifyInvalidName() {
        textLayoutName.setError(getString(R.string.register_no_name));
    }

    @Override
    public void notifyInvalidEmail() {
        textLayoutEmail.setError(getString(R.string.register_no_email));
    }

    @Override
    public void notifyInvalidSSN() {
        textLayoutSSN.setError(getString(R.string.register_no_ssn));
    }

    @Override
    public void notifyInvalidPassword() {
        textLayoutPassword.setError(getString(R.string.register_no_password));
    }

    private void resetErrorOnTextInput() {
        textInputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textLayoutName.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        textInputSSN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textLayoutSSN.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
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
}
