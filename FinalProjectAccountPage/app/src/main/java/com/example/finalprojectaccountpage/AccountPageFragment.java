package com.example.finalprojectaccountpage;

import static androidx.core.app.ActivityCompat.recreate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewAnimator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.finalprojectaccountpage.firebasestuff.UserModal;

import java.util.Locale;
import java.util.Objects;


public class AccountPageFragment extends Fragment
{

    //default empty constructor required by fragments
    //--------------------------//
    public AccountPageFragment()
    {

    }
    //--------------------------//

    //these strings store the users data (pulled from firebase).
    //First, the user enters their log in data in the LoginFragment activity.
    //Second, the LoginFragment assigns that data to these strings
    //---------------------------------------//
    private static UserModal currentUser;
    public static String firstName;
    public static String lastName;
    public static String emailAddress;
    public static String userPassword;
    //---------------------------------------//

    //string array for the language selection spinner
    //-------------------------------------------------//
    String[] langs = new String[]{"English", "French"};
    //-------------------------------------------------//

    //string array for the language codes
    //-------------------------------------------//
    String[] langCodes = new String[]{"en", "fr"};
    //-------------------------------------------//



    //Declarations for the views contained in MainActivity
    //---------------------------------------------------//
    Button mservButton;
    Button asettButton;
    Button pinfoButton;
    Button logoutButton;
    Button appSettingsloginButton;
    TextView fNamelNameTextView;
    TextView emailTextView;
    ImageView PPictureImageView;
    Spinner spinner;
    RadioGroup RGroup;
    ViewAnimator viewAnimator;
    Button backButton;
    //---------------------------------------------------//




    private SharedPreferences.OnSharedPreferenceChangeListener listener;


    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        //default stuff
        //-------------------------------------------------------------------------------------//
        super.onCreate(savedInstanceState);
        //-------------------------------------------------------------------------------------//
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_account_page, container, false);

        //code for building and inflating the app settings dialog
        //----------------------------------------------------------------------------//
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity());
        AlertDialog AppSettingsDialog = dialogBuilder.create();
        LayoutInflater appSettingsInflater = getLayoutInflater();
        View dialogView = appSettingsInflater.inflate(R.layout.app_settings_dialog, null);
        AppSettingsDialog.setView(dialogView);

        //----------------------------------------------------------------------------//

        //assigning the views to the proper variables
        //----------------------------------------------------------------//
        mservButton = view.findViewById(R.id.my_services_button);
        asettButton = view.findViewById(R.id.account_settings_button);
        pinfoButton = view.findViewById(R.id.personal_information_button);
        logoutButton = view.findViewById(R.id.logout_button);
        appSettingsloginButton = view.findViewById(R.id.LogInButton);
        fNamelNameTextView = view.findViewById(R.id.FirstNameLastNameTextView);
        emailTextView = view.findViewById(R.id.EmailTextView);
        PPictureImageView = view.findViewById(R.id.ProfilePictureImageView);
        spinner = dialogView.findViewById(R.id.LangSelectSpinner);
        RGroup = dialogView.findViewById(R.id.DModeRadioGroup);
        viewAnimator = view.findViewById(R.id.viewAnimator);
        backButton = view.findViewById(R.id.goback_button);
        //----------------------------------------------------------------//

        UserModal currentUser = UserModal.getCurrentUser();
        // If user exists
        if(currentUser.getEmail() != null)
        {
            firstName = currentUser.getFirstName();
            lastName = currentUser.getLastName();
            emailAddress = currentUser.getEmail();
            userPassword = currentUser. getPassword();
        }

        //----------------------------------------------------------------//

        //assigning variables to the three radio buttons contained in the app settings dialog
        //these radio buttons control the dark mode setting within the app
        //----------------------------------------------------------------------------------//
        RadioButton button1 = dialogView.findViewById(R.id.radio_DModeOn);
        RadioButton button2 = dialogView.findViewById(R.id.radio_DModeOff);
        RadioButton button3 = dialogView.findViewById(R.id.radio_sysControlled);
        //----------------------------------------------------------------------------------//

        //language selection spinner setup code
        //-------------------------------------------------------------------------------------------------------------------------//
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, langs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //-------------------------------------------------------------------------------------------------------------------------//

        //Creating the login/sign up dialog
        //---------------------------------------------------//
        LoginSignupDialog myDialog = new LoginSignupDialog();
        //---------------------------------------------------//

        //sharedPreferences is a small library that stores data on-device even after the app is closed
        //these two lines of code create the SharedPreferences library, and the SharedPreferences editor (self explanatory)
        //------------------------------------------------------------------------------------------------//
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //------------------------------------------------------------------------------------------------//

        //then we create a boolean that contains the value of the key "isLoggedIn", which is stored in the sharedPreferences library
        //this boolean tells the app whether or not the user is logged in
        //-------------------------------------------------------------------------//
        boolean LoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        //-------------------------------------------------------------------------//

        //checks if the SharedPreferences "isLoggedIn" boolean is true or false
        //this if else block only checks upon the creation of the AccountPageFragment, I.E upon opening the account page
        //-----------//
        if (!LoggedIn)
        //-----------//
        {
            //if the user is not logged in, the login dialog is shown
            //----------------------------------------------//
            //myDialog.show(getChildFragmentManager(), "login_dialog");
            //----------------------------------------------//

            //sets the buttons and textviews contained in the account
            //info page to be invisible while the login dialog is shown
            //----------------------------------------------//
            mservButton.setVisibility(View.GONE);
            asettButton.setVisibility(View.VISIBLE);
            pinfoButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
            fNamelNameTextView.setVisibility(View.GONE);
            emailTextView.setVisibility(View.GONE);
            appSettingsloginButton.setVisibility(View.VISIBLE);
            //----------------------------------------------//

            //this line of code means that the login dialog can not be dismissed by tapping outside of it
            //---------------------------//
            //myDialog.setCancelable(true);
            //---------------------------//


        }

        else
        {
            //if the user is already logged in, the users first name, last name, and email address are pulled
            //from the firebase and the corresponding TextViews are updated with that information
            //-----------------------------------------------------//
            fNamelNameTextView.setText(firstName + " " + lastName);
            emailTextView.setText(emailAddress);
            //-----------------------------------------------------//
        }

        appSettingsloginButton.setOnClickListener(view1 -> myDialog.show(getChildFragmentManager(), "login_dialog"));


        //constantly checks the value of the SharedPreferences "isLoggedIn" boolean.
        //if the boolean changes to true, the login dialog is dismissed and the buttons and TextViews are made visible
        //if the boolean changes to false, the login dialog is shown and the buttons and TextViews are made invisible
        //-------------------------------------------------------------------------------------------------------------------------------------------//
        listener = (sharedPreferences1, key) ->
        {
            assert key != null;
            if (key.equals("isLoggedIn"))
            {

                if (sharedPreferences1.getBoolean("isLoggedIn", false))
                {
                    //the buttons and textview in the account info page are made visible
                    //----------------------------------------------//
                    mservButton.setVisibility(View.VISIBLE);
                    asettButton.setVisibility(View.VISIBLE);
                    pinfoButton.setVisibility(View.VISIBLE);
                    logoutButton.setVisibility(View.VISIBLE);
                    fNamelNameTextView.setVisibility(View.VISIBLE);
                    emailTextView.setVisibility(View.VISIBLE);
                    appSettingsloginButton.setVisibility(View.GONE);
                    //----------------------------------------------//

                    //the firstName and lastName variables (the ones pulled from firebase) are assigned to the firstname and lastname textview
                    //----------------------------------------------------//
                    fNamelNameTextView.setText(firstName + " " + lastName);
                    //----------------------------------------------------//

                    //and the email variable is assigned to the email textview
                    //----------------------------------//
                    emailTextView.setText(emailAddress);
                    //----------------------------------//

                    //lastly, the dialog is dismissed
                    //-----------------//
                    if (isAdded())
                    {
                        myDialog.dismiss();
                    }
                    //-----------------//
                }

                else if (!sharedPreferences1.getBoolean("isLoggedIn", false))
                {
                        //shows the dialog
                        //----------------------------------------------//
                        if (isAdded())
                        {
                            myDialog.show(requireActivity().getSupportFragmentManager(), "login_dialog");
                        }
                        //----------------------------------------------//

                        //sets the buttons and TextViews contained in the account
                        //info page to be invisible while the login dialog is shown
                        //----------------------------------------------//
                        mservButton.setVisibility(View.GONE);
                        pinfoButton.setVisibility(View.GONE);
                        logoutButton.setVisibility(View.GONE);
                        fNamelNameTextView.setVisibility(View.GONE);
                        emailTextView.setVisibility(View.GONE);
                        appSettingsloginButton.setVisibility(View.VISIBLE);
                        //----------------------------------------------//

                        //this line of code means that the login dialog can be dismissed by tapping outside of it
                        //---------------------------//
                        myDialog.setCancelable(true);
                        //---------------------------//
                }
            }

        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        //-------------------------------------------------------------------------------------------------------------------------------------------//


        //simple logout button onClickListener. If the user clicks logout, the SharedPreferences "isLoggedIn" boolean is set to false
        //therefore triggering the dialog to popup and the views be made invisible
        //----------------------------------------------------------------------------------//
        logoutButton.setOnClickListener(view12 ->
        {
            //sets the SharedPreferences "isLoggedIn" boolean to false when the logout button is clicked
            //-------------------------------------------------------------------------//
            editor.putBoolean("isLoggedIn", false);
            editor.putString("UserFirstName", null);
            editor.putString("UserLastName", null);
            editor.putString("UserEmail", null);
            editor.putString("UserPassword", null);
            editor.putString("UserProfilePicture", null);
            editor.apply();
            UserModal.setCurrentUser(new UserModal(null,null,null,null,null));
            //-------------------------------------------------------------------------//
        });
        //----------------------------------------------------------------------------------//



        asettButton.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view)
            {
                AppSettingsDialog.setOnShowListener(dialogInterface ->
                {
                    //when the app settings dialog is opened, the sharedPreferences "DarkModeSetting" value is checked
                    //--------------------------------------------------------------------------------------------------------------------------//
                    if (sharedPreferences.getInt("DarkModeSetting", 0) == 0)
                    {
                        //if DarkModeSetting is 0, the dialog background is set to the dark mode background, to coincide with dark mode being enabled
                        //-------------------------------------------------------------------------------------------------------------------//
                        Objects.requireNonNull(AppSettingsDialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_background_dm);
                        button1.setChecked(true);
                        //-------------------------------------------------------------------------------------------------------------------//
                    }
                    else if (sharedPreferences.getInt("DarkModeSetting", 0) == 1)
                    {
                        //if DarkModeSetting is 1, the dialog background is set to the light mode background, to coincide with dark mode being disabled
                        //----------------------------------------------------------------------------------------------------------------//
                        Objects.requireNonNull(AppSettingsDialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_background);
                        button2.setChecked(true);
                        //----------------------------------------------------------------------------------------------------------------//
                    }
                    else if (sharedPreferences.getInt("DarkModeSetting", 0) == 2)
                    {
                        //if the DarkModeSetting is 2, a little bit more work is required.
                        //-----------------------------------------------------------------------------------------------------------------------//
                        button3.setChecked(true);


                        //using an if statement, we check if the System dark mode setting is enabled or not
                        //----------------------------------------------------------------------------------------------------------------------//
                        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

                        switch (currentNightMode) {
                            case Configuration.UI_MODE_NIGHT_NO:
                                Objects.requireNonNull(AppSettingsDialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_background);
                                break;
                            case Configuration.UI_MODE_NIGHT_YES:
                                Objects.requireNonNull(AppSettingsDialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_background_dm);
                                break;
                        }
                        //-----------------------------------------------------------------------------------------------------------------------//
                    }
                    //--------------------------------------------------------------------------------------------------------------------------//

                    if (sharedPreferences.getInt("selectedLanguage", 0) == 0)
                    {
                        spinner.setSelection(0);
                    }
                    else if (sharedPreferences.getInt("selectedLanguage", 0) == 1)
                    {
                        spinner.setSelection(1);
                    }

                    //this OnCancelListener is called when the user dismisses the dialog
                    //-------------------------------------------------------------------------//
                    AppSettingsDialog.setOnCancelListener(dialogInterface1 -> {

                        if (button1.isChecked())
                        {
                            editor.putInt("DarkModeSetting", 0);
                            editor.apply();
                            if (isAdded()) {
                                AppCompatActivity activity = (AppCompatActivity) getActivity();
                                if (activity != null) {
                                    activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                }
                            }


                        }
                        else if (button2.isChecked())
                        {
                            editor.putInt("DarkModeSetting", 1);
                            editor.apply();
                            if (isAdded()) {
                                AppCompatActivity activity = (AppCompatActivity) getActivity();
                                if (activity != null) {
                                    activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                }
                            }
                        }
                        else if (button3.isChecked())
                        {
                            editor.putInt("DarkModeSetting", 2);
                            editor.apply();
                            if (isAdded()) {
                                AppCompatActivity activity = (AppCompatActivity) getActivity();
                                if (activity != null) {
                                    activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                                }
                            }
                        }
                        recreate(requireActivity());
                    });
                    //-------------------------------------------------------------------------//
                });
                AppSettingsDialog.show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                String selectedCode = langCodes[position];

                Locale locale;
                Configuration config;

                switch (selectedItem)
                {
                    case "English":
                        editor.putInt("selectedLanguage", 0);
                        editor.apply();
                        locale = new Locale(selectedCode);
                        Locale.setDefault(locale);
                        config = getResources().getConfiguration();
                        config.setLocale(locale);
                        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                        break;
                    case "French":
                        editor.putInt("selectedLanguage", 1);
                        editor.apply();
                        locale = new Locale(selectedCode);
                        Locale.setDefault(locale);
                        config = getResources().getConfiguration();
                        config.setLocale(locale);
                        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                        break;




                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        mservButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyServicesPage.class));
            }
        });

        pinfoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Animation outAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left);
                viewAnimator.setOutAnimation(outAnimation);

                Animation inAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
                viewAnimator.setInAnimation(inAnimation);

                viewAnimator.showNext();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Animation inAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left);
                viewAnimator.setInAnimation(inAnimation);

                Animation outAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right);
                viewAnimator.setOutAnimation(outAnimation);

                viewAnimator.showPrevious();
            }
        });


                // Inflate the layout for this fragment
        return view;
    }
}

