package com.example.finalprojectaccountpage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectaccountpage.adapters.ChatProfileRowAdapter;
import com.example.finalprojectaccountpage.firebasestuff.UserModal;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ChatPageFragment extends Fragment {

    ChatProfileRowAdapter adapter;
    RecyclerView recyclerView;

    public ChatPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        // Linking elements
        recyclerView = view.findViewById(R.id.chatRecyclerViewFragmentPage);

        // Check if user is logged in
        if(UserModal.getCurrentUser().getEmail() != null) {
            // Getting chats from database
            Query query = FirebaseFirestore.getInstance().collection("Users").whereNotEqualTo("email", UserModal.getCurrentUser().getEmail());
            FirestoreRecyclerOptions<UserModal> options = new FirestoreRecyclerOptions.Builder<UserModal>()
                    .setQuery(query, UserModal.class).build();

            adapter = new ChatProfileRowAdapter(options, getActivity().getApplicationContext());

            // Setting up recycler view
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }
    }


    // Setting for optimization
    @Override
    public void onStart() {
        super.onStart();
        if(adapter !=null)
        {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter !=null)
        {
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter !=null)
        {
            adapter.startListening();
        }
    }
}