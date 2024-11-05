package com.example.finalprojectmobile2;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.finalprojectmobile2.fragments.FragmentAdapter;
import com.example.finalprojectmobile2.model.UserModel;
import com.example.finalprojectmobile2.util.FirebaseUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    // ------------ NAVIGATION BAR VARIABLES -----------
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    // -------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // ----------------------- NAVIGATION BAR (START) -----------------------

        // Binding UI to java elements
        tabLayout =findViewById(R.id.tabLayout);
        viewPager2 =findViewById(R.id.viewPager2Fragments);

        //Create Adapter and set it for ViewPager2
        FragmentAdapter newsPagerAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(newsPagerAdapter);

        //Set up a listener to switch between tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        // ----------------------- NAVIGATION BAR (END) -----------------------


        // TEST DATABASE CONNECTIVITY
        String userId = "test1";

        FirebaseUtil.getObjectUser(userId);

    }

}