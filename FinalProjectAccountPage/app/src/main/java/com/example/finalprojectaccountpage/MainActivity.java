package com.example.finalprojectaccountpage;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectaccountpage.firebasestuff.UserModal;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
{


    BottomNavigationView bottomNavigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        String[] selectedCode = {"en", "fr"};
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // --------- Get User info ---------
        UserModal.setCurrentUser(new UserModal(
                sharedPreferences.getString("UserFirstName", null),
                sharedPreferences.getString("UserLastName", null),
                sharedPreferences.getString("UserEmail", null),
                sharedPreferences.getString("UserPassword", null),
                sharedPreferences.getString("UserProfilePicture", null)
        ));

        // ---------------------------------


        int selectedLanguage = sharedPreferences.getInt("selectedLanguage", 0);
        Locale locale;
        Configuration config;

        if (selectedLanguage == 0)
        {
            locale = new Locale(selectedCode[selectedLanguage]);
            Locale.setDefault(locale);
            config = getResources().getConfiguration();
            config.setLocale(locale);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        }
        else if (selectedLanguage == 1)
        {
            locale = new Locale(selectedCode[selectedLanguage]);
            Locale.setDefault(locale);
            config = getResources().getConfiguration();
            config.setLocale(locale);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }



        int DarkModeSetting = sharedPreferences.getInt("DarkModeSetting", 0);
        if (DarkModeSetting == 0)
        {
            AppCompatActivity activity = (AppCompatActivity) this;
            activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else if (DarkModeSetting == 1)
        {
            AppCompatActivity activity = (AppCompatActivity) this;
            activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if (DarkModeSetting == 2)
        {
            AppCompatActivity activity = (AppCompatActivity) this;
            activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }


        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                if (item.getItemId() == R.id.home)
                {
                    item.setChecked(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new HomePageFragment()).commit();
                }
                else if (item.getItemId() == R.id.chat)
                {
                    item.setChecked(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new ChatPageFragment()).commit();
                }
                else if (item.getItemId() == R.id.account)
                {
                    item.setChecked(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new AccountPageFragment()).commit();
                }
                return false;
            }
        });






    }

}