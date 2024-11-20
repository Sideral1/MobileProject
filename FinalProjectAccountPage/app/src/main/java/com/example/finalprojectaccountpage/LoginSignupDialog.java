package com.example.finalprojectaccountpage;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import android.view.WindowManager;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class LoginSignupDialog extends DialogFragment
{
    EditText email;
    EditText password;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int DarkModeSetting = sharedPreferences.getInt("DarkModeSetting", 0);

        //creating the dialog using a builder and inflating it
        //-------------------------------------------------------------------//
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.login_signup_dialog_tabs, null);
        //-------------------------------------------------------------------//

        //assigning the tablayout and viewpager to a variable
        //---------------------------------------------------------------------------------------//
        com.google.android.material.tabs.TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        //---------------------------------------------------------------------------------------//

        // Create and set an adapter for the ViewPager2
        //--------------------------------------------------------------------------//
        LoginSignupPagerAdapter adapter = new LoginSignupPagerAdapter(this);
        viewPager.setAdapter(adapter);
        //--------------------------------------------------------------------------//



        // Connect the TabLayout and ViewPager2
        //-----------------------------------------//
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0)
                    {
                        tab.setText(getString(R.string.login));
                    }
                    else
                    {
                        tab.setText(getString(R.string.sign_up));
                    }
                }).attach();
        //-----------------------------------------//

        //setting the inflated dialog as the view of the dialog builder
        //--------------------//
        builder.setView(view);
        //--------------------//

        //creating the finished dialog
        //-----------------------------------//
        AlertDialog dialog = builder.create();
        //-----------------------------------//

        //gives the dialog rounded corners
        //-----------------------------------------------------------------------------//
        if (DarkModeSetting == 1)
        {
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_background);
        }
        else if (DarkModeSetting == 0)
        {

            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_background_dm);
        }
        else if (DarkModeSetting == 2)
        {

            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_background);
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_background_dm);
                    break;
            }

            //-----------------------------------------------------------------------------//
        }



        //changes the height of the dialog depending on which tab is clicked
        //-----------------------------------------------------------------------------------------------------------------------------//
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                int position = tab.getPosition();

                switch (position)
                {
                    case 0:
                        Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.WRAP_CONTENT, 1200);
                        break;

                    case 1:

                        Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });
        //-----------------------------------------------------------------------------------------------------------------------------//

        //shows the dialog on screen
        //------------//
        return dialog;
        //------------//
    }

    // Inner class for the ViewPager2 adapter
    //-----------------------------------------------------------------------//
    private static class LoginSignupPagerAdapter extends FragmentStateAdapter
    {

        public LoginSignupPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position)
        {
            if (position == 0)
            {
                return new LoginFragment();
            }
            else
            {
                return new SignUpFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2; // Two tabs (Login and Signup)
        }
    }
    //-----------------------------------------------------------------------//
}

