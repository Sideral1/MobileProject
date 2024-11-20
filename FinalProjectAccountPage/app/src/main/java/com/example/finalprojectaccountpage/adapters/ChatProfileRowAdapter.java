package com.example.finalprojectaccountpage.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectaccountpage.ChatMessageActivity;
import com.example.finalprojectaccountpage.R;
import com.example.finalprojectaccountpage.firebasestuff.UserModal;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatProfileRowAdapter extends FirestoreRecyclerAdapter<UserModal,ChatProfileRowAdapter.UserModelViewHolder > {

    Context context;
    public ChatProfileRowAdapter(@NonNull FirestoreRecyclerOptions<UserModal> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModal model) {
        String fullName = model.getFirstName() + " " + model.getLastName();
        holder.usernameTextView.setText(fullName);

        // Open chat if profile is clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatMessageActivity.class);
                intent.putExtra("name", fullName);
                intent.putExtra("email", model.getEmail());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_profile_row, parent, false);
        return  new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder
    {
        TextView usernameTextView;
        ImageView profilePicImageView;


        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.user_name_text);
            profilePicImageView = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
