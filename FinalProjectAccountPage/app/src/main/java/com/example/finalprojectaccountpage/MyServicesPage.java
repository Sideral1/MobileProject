package com.example.finalprojectaccountpage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyServicesPage extends AppCompatActivity
{
    RecyclerView RecView;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services_page);
        EdgeToEdge.enable(this);


        RecView = findViewById(R.id.RecyclerView);
        TextView noItems = findViewById(R.id.NoItemsTextView);



        AppCompatActivity activity = this;
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int darkmodesetting = sharedPreferences.getInt("DarkModeSetting", 0);

        if (darkmodesetting == 0)
        {
            activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else if (darkmodesetting == 1)
        {
            activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }




        List<String> serviceNames = new ArrayList<>();


        List<String> servicePrices = new ArrayList<>();


        List<String> serviceAvailability = new ArrayList<>();

        if (serviceNames.isEmpty())
        {
            noItems.setVisibility(View.VISIBLE);
        }
        else
        {
            noItems.setVisibility(View.GONE);
        }

        adapter = new RecyclerViewAdapter(serviceNames, servicePrices, serviceAvailability);
        RecView.setAdapter(adapter);
        RecView.setLayoutManager(new LinearLayoutManager(this));

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver()
        {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount)
            {
                super.onItemRangeRemoved(positionStart, itemCount);
                if (adapter.getItemCount() == 0)
                {
                    noItems.setVisibility(View.VISIBLE);
                }
                else
                {
                    noItems.setVisibility(View.GONE);
                }
            }
        });









    }
}