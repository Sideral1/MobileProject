package com.example.finalprojectaccountpage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private List<String> serviceNames;
    private List<String> servicePrices;
    private List<String> serviceAvailability;





    public RecyclerViewAdapter(List<String> serviceNames, List<String> servicePrices, List<String> serviceAvailability )
    {
        this.serviceNames = serviceNames;
        this.servicePrices = servicePrices;
        this.serviceAvailability = serviceAvailability;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView serviceName;
        public TextView servicePrices;
        public TextView serviceAvailability;
        public ImageButton deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.Service_Name);
            servicePrices = itemView.findViewById(R.id.service_Price);
            serviceAvailability = itemView.findViewById(R.id.service_Availability);
            LinearLayout container = itemView.findViewById(R.id.LinearLayout);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.serviceName.setText(serviceNames.get(position));
        holder.servicePrices.setText(servicePrices.get(position));
        holder.serviceAvailability.setText(serviceAvailability.get(position));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceNames.remove(holder.getAbsoluteAdapterPosition());
                servicePrices.remove(holder.getAbsoluteAdapterPosition());
                serviceAvailability.remove(holder.getAbsoluteAdapterPosition());
                notifyItemRemoved(holder.getAbsoluteAdapterPosition());
            }
        });
    }
        @Override
    public int getItemCount()
    {
        return serviceNames.size();
    }
}