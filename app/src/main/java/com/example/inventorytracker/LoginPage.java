package com.example.inventorytracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends Fragment implements View.OnClickListener
{
    View myView;
    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;
    private Button createAccountButton;
    String email;
    String password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.login_page, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Core.auth = FirebaseAuth.getInstance();
        loginButton = (Button) getActivity().findViewById(R.id.loginButton);
        createAccountButton = (Button)getActivity().findViewById(R.id.createAccountButton);
        setClickListeners();
        emailField = (EditText) getActivity().findViewById(R.id.emailField);
        passwordField = (EditText) getActivity().findViewById(R.id.passwordField);
    }

    private void startSignIn()
    {
        setStrings();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            if(TextUtils.isEmpty(email))
            {
                emailField.setError("Required");
            }

            if(TextUtils.isEmpty(password))
            {
                passwordField.setError("Required");
            }
            Toast.makeText(getActivity(), "Login fields are empty", Toast.LENGTH_LONG).show();
        }
        else {
            Core.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = Core.auth.getCurrentUser();
                        Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, new ViewInventoryFragment()).addToBackStack("tag").commit();
                    }
                    else
                        {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getActivity(), "Authentication failed. \nWrong username or password", Toast.LENGTH_LONG).show();
                        }
                }
            });
        }
    }

    private void createAccount()
    {
        setStrings();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            if(TextUtils.isEmpty(email))
            {
                emailField.setError("Required");
            }

            if(TextUtils.isEmpty(password))
            {
                passwordField.setError("Required");
            }
            Toast.makeText(getActivity(), "Login fields are empty, cannot create account", Toast.LENGTH_LONG).show();
        }
        else
        {
            Core.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = Core.auth.getCurrentUser();
                        Toast.makeText(getActivity(), "Account created. Login Successful", Toast.LENGTH_LONG).show();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, new ViewInventoryFragment()).addToBackStack("tag").commit();
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getActivity(), "Authentication failed. \nPlease use an email address", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    public void onClick(View v)
    {
        startSignIn();
    }

    public void setClickListeners()
    {
        loginButton.setOnClickListener(this);

        createAccountButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    public void setStrings()
    {
        email = emailField.getText().toString();
        password = passwordField.getText().toString();
    }
}
