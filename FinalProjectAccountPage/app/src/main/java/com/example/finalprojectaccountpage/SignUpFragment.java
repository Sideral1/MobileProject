package com.example.finalprojectaccountpage;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectaccountpage.firebasestuff.UserModal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignUpFragment extends Fragment
{
    // Firebase variables
    private FirebaseFirestore db;
    private String firstNameString;
    private String lastNameString;
    private String emailString;
    private String passwordString;


    // UI Variables
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText confirmPassword;


    TextInputLayout passwordInputLayout;
    TextInputLayout confirmPasswordInputLayout;

    TextView pwordLength;
    TextView pwordNums;
    TextView pwordCaps;

    Button signupButton;

    private boolean validFirstName = false;
    private boolean validLastName = false;
    private boolean validEmail = false;
    private boolean validPassword = false;
    private boolean validConfirmPassword = false;


    public SignUpFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        //assigning the different views contained within the dialog
        //----------------------------------------------------------//
        firstName = view.findViewById(R.id.firstNameText);
        lastName = view.findViewById(R.id.lastNameText);
        email = view.findViewById(R.id.emailText);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        signupButton = view.findViewById(R.id.signUpButton);
        pwordLength = view.findViewById(R.id.pwordLength);
        pwordNums = view.findViewById(R.id.pwordNums);
        pwordCaps = view.findViewById(R.id.pwordCaps);
        passwordInputLayout = view.findViewById(R.id.PasswordInputLayout);
        confirmPasswordInputLayout = view.findViewById(R.id.confirmPasswordInputLayout);
        //---------------------------------------------------------//

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                //if else statements controlling the validation logic of the sign up text fields
                if (password.getText().toString().length() >= 8)
                {
                    pwordLength.setTextColor(Color.GREEN);
                }
                else
                {
                    pwordLength.setTextColor(Color.RED);
                }

                if (password.getText().toString().matches(".*\\d.*"))
                {
                    pwordNums.setTextColor(Color.GREEN);
                }
                else
                {
                    pwordNums.setTextColor(Color.RED);
                }

                if (password.getText().toString().matches(".*[A-Z].*"))
                {
                    pwordCaps.setTextColor(Color.GREEN);
                }
                else
                {
                    pwordCaps.setTextColor(Color.RED);
                }



            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (!password.getText().toString().equals(confirmPassword.getText().toString()))
                {
                    confirmPassword.setError(getString(R.string.password_mismatch));
                    confirmPasswordInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    validConfirmPassword = false;
                }
                else if (password.getText().toString().equals(confirmPassword.getText().toString()))
                {
                    confirmPassword.setError(null);
                    confirmPasswordInputLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                    validConfirmPassword = true;
                }
            }
        };

        password.addTextChangedListener(watcher);
        confirmPassword.addTextChangedListener(watcher);

        signupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!firstName.getText().toString().trim().contains(" ") && !firstName.getText().toString().trim().isEmpty())
                {
                    validFirstName = true;
                    firstNameString = firstName.getText().toString().trim();
                }
                else if (firstName.getText().toString().trim().contains(" "))
                {
                    validFirstName = false;
                    firstName.setError(getString(R.string.first_name_cannot_contain_spaces));
                }
                else if (firstName.getText().toString().trim().isEmpty())
                {
                    validFirstName = false;
                    firstName.setError(getString(R.string.first_name_cannot_be_blank));
                }
                else
                {
                    validFirstName = false;
                }

                if (!lastName.getText().toString().trim().contains(" ") && !lastName.getText().toString().trim().isEmpty())
                {
                    validLastName = true;
                    lastNameString = lastName.getText().toString().trim();
                }
                else if (lastName.getText().toString().trim().contains(" "))
                {
                    validLastName = false;
                    lastName.setError(getString(R.string.last_name_cannot_contain_spaces));
                }
                else if (lastName.getText().toString().trim().isEmpty())
                {
                    validLastName = false;
                    lastName.setError(getString(R.string.last_name_cannot_be_blank));
                }
                else
                {
                    validLastName = false;
                }

                if (email.getText().toString().trim().matches(emailPattern) && !email.getText().toString().trim().contains(" ") && !email.getText().toString().trim().isEmpty())
                {
                    validEmail = true;
                    emailString = email.getText().toString().trim();
                }
                else if (!email.getText().toString().trim().matches(emailPattern))
                {
                    validEmail = false;
                    email.setError(getString(R.string.invalid_email_format));
                }
                else if (email.getText().toString().trim().contains(" "))
                {
                    validEmail = false;
                    email.setError(getString(R.string.email_cannot_contain_spaces));
                }
                else if (email.getText().toString().trim().isEmpty())
                {
                    validEmail = false;
                    email.setError(getString(R.string.email_cannot_be_blank));
                }
                else
                {
                    validEmail = false;
                }



                if (!password.getText().toString().trim().isEmpty() && password.getText().toString().trim().length() >= 8 && password.getText().toString().trim().matches(".*\\d.*") && password.getText().toString().trim().matches(".*[A-Z].*"))
                {
                    validPassword = true;
                    passwordString = password.getText().toString().trim();
                }
                else if (password.getText().toString().trim().isEmpty())
                {
                    passwordInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    password.setError(getString(R.string.password_cannot_be_blank));
                    password.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                        {
                            password.setError(null);
                            passwordInputLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }

                if (validFirstName && validLastName && validEmail && validPassword && validConfirmPassword)
                {
                    // Adding user to the database - Getting instance
                    db = FirebaseFirestore.getInstance();

                    // Collection that will store users info.
                    CollectionReference dbUsers = db.collection("Users");

                    // Checking if user is already registered
                    dbUsers.document(emailString).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Toast.makeText(getActivity(), "Email already registered", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Adding our data to User object class.
                                    UserModal userModal = new UserModal(firstNameString, lastNameString, emailString, passwordString, null);

                                    dbUsers.document(emailString).set(userModal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getActivity(), "Account Created", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Error creating account", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Error connecting to database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }
}