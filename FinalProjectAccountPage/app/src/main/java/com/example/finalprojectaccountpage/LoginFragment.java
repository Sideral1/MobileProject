package com.example.finalprojectaccountpage;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectaccountpage.firebasestuff.UserModal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class LoginFragment extends Fragment
{
    UserModal currentUser;
    FirebaseFirestore db;

    Button loginButton;
    TextInputLayout emaillayout;
    TextInputLayout passwordlayout;
    TextInputEditText email;
    TextInputEditText password;




    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        //assigning the different text boxes and buttons contained within the dialog
        //----------------------------------------------------------//
        loginButton = (Button) view.findViewById(R.id.loginButton);
        email = (TextInputEditText) view.findViewById(R.id.email);
        password = (TextInputEditText) view.findViewById(R.id.password);
        emaillayout = (TextInputLayout) view.findViewById(R.id.EmailInputLayout);
        passwordlayout = (TextInputLayout) view.findViewById(R.id.PasswordInputLayout);

        //----------------------------------------------------------//
        // Getting current user reference
        currentUser = UserModal.getCurrentUser();
        //----------------------------------------------------------//

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                emaillayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                passwordlayout.setEndIconVisible(true);
                passwordlayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //this code controls the validation of the email and password entered into the text boxes by comparing it to the emails and passwords stored in the variable
        loginButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                //stores the entered email and password into variables
                //----------------------------------------------------//
                String enteredEmail = Objects.requireNonNull(email.getText()).toString();
                String enteredPassword = Objects.requireNonNull(password.getText()).toString();
                //----------------------------------------------------//

                //ensures that the email and password field are not empty
                //----------------------------------------------------------------------------------------//
                if(enteredEmail.isEmpty() && enteredPassword.isEmpty())
                {
                    password.setError(getString(R.string.password_cannot_be_blank));
                    passwordlayout.setEndIconVisible(false);
                }
                if (enteredEmail.isEmpty())
                {
                    email.setError(getString(R.string.email_cannot_be_blank));

                }
                else if (enteredPassword.isEmpty())
                {
                    passwordlayout.setError(getString(R.string.password_cannot_be_blank));
                    passwordlayout.setEndIconVisible(false);
                }
                //----------------------------------------------------------------------------------------//

                //if both are not empty, move on to validating the email and password
                //------------------------------------------------------------------------//
                else
                {
                    // Retrieving user's data from database
                    db = FirebaseFirestore.getInstance();

                    // Collection that stores user's info.
                    CollectionReference dbUsers = db.collection("Users");

                    // Checking if user is already registered == email matches
                    dbUsers.document(enteredEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                DocumentSnapshot document = task.getResult();
                                // if document exists, then email input by user matches
                                if (document.exists()) {

                                    UserModal userModal = document.toObject(UserModal.class);

                                    // Both email and password matches
                                    if(userModal.getPassword().equals(enteredPassword))
                                    {
                                        AccountPageFragment.firstName = userModal.getFirstName();
                                        AccountPageFragment.lastName = userModal.getLastName();
                                        AccountPageFragment.emailAddress = userModal.getEmail();
                                        AccountPageFragment.userPassword = userModal.getPassword();

                                        // Save current user locally
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.putString("UserFirstName", userModal.getFirstName());
                                        editor.putString("UserLastName", userModal.getLastName());
                                        editor.putString("UserEmail", userModal.getEmail());
                                        editor.putString("UserPassword", userModal.getPassword());
                                        editor.putString("UserProfilePicture", userModal.getProfilePicture());
                                        editor.apply();

                                        // Getting current user
                                        UserModal.setCurrentUser(userModal);

                                        Toast.makeText(getActivity(), "Welcome back " + userModal.getFirstName() + "!", Toast.LENGTH_SHORT).show();
                                    }


                                } else {
                                    Toast.makeText(getActivity(), "Account does not exist", Toast.LENGTH_SHORT).show();
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