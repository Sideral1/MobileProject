package com.example.finalprojectaccountpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectaccountpage.adapters.ChatMessagesAdapter;
import com.example.finalprojectaccountpage.firebasestuff.ChatMessageModal;
import com.example.finalprojectaccountpage.firebasestuff.ChatRoomModal;
import com.example.finalprojectaccountpage.firebasestuff.UserModal;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class ChatMessageActivity extends AppCompatActivity {

    // Firebase variables
    String chatRoomId;
    ChatRoomModal chatRoomModal;
    ChatMessagesAdapter chatMessagesAdapter;

    String targetName;
    String targetEmail;

    ImageView backImg;
    ImageView sendBtn;
    TextView username;
    EditText inputText;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.chat_message_activity);

        // Getting target's info
        targetName = getIntent().getStringExtra("name");
        targetEmail = getIntent().getStringExtra("email");

        // Linking UI
        backImg = findViewById(R.id.chat_message_activity_back_btn);
        sendBtn = findViewById(R.id.chat_message_activity_message_send_btn);
        username = findViewById(R.id.chat_message_activity_name);
        inputText = findViewById(R.id.chat_message_activity_input);
        recyclerView = findViewById(R.id.chat_message_activity_recycler_view);
        // -------------------------------------------------------------------

        // Get target's name
        username.setText(targetName);

        //---------------------------- Firebase Setup Start ----------------------------

        // Creating chat room
        int idCurrentUser = UserModal.getCurrentUser().getEmail().hashCode();
        int idTargetUser= targetEmail.hashCode();
        chatRoomId = "";

        if(idCurrentUser < idTargetUser)
        {
            chatRoomId = idCurrentUser+"_"+idTargetUser;
        }
        else
        {
            chatRoomId = idTargetUser+"_"+idCurrentUser;
        }

        // Getting chat room from database
        FirebaseFirestore.getInstance().collection("ChatRooms").document(chatRoomId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    // Get chat room modal if exists
                    chatRoomModal = task.getResult().toObject(ChatRoomModal.class);

                    // If exists chat room does not exist, creates one
                    if(chatRoomModal==null)
                    {
                        chatRoomModal = new ChatRoomModal(
                            chatRoomId,
                            Arrays.asList(UserModal.getCurrentUser().getEmail(), targetEmail),
                            Timestamp.now(),
                            ""
                        );

                        // Creating chat room
                        FirebaseFirestore.getInstance().collection("ChatRooms").document(chatRoomId).set(chatRoomModal);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error connecting to database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ---------------------------- Firebase Setup End ----------------------------

        // Pulling messages from database
        Query query = FirebaseFirestore.getInstance().
                collection("ChatRooms").document(chatRoomId)
                .collection("Chats")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        if(query.get().isSuccessful())
        {
            Log.d("TEST", "CALMA");
        }
        else{
            Log.d("TEST", "ACHEI");
        }

        FirestoreRecyclerOptions<ChatMessageModal> options = new FirestoreRecyclerOptions.Builder<ChatMessageModal>()
                .setQuery(query,ChatMessageModal.class).build();

        chatMessagesAdapter = new ChatMessagesAdapter(options,getApplicationContext());

        // Messages in the right order
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);

        // Initiating recycler view
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(chatMessagesAdapter);
        chatMessagesAdapter.startListening();

        // Automatically scroll when recycler view is updated
        chatMessagesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });



        // Return to home page
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });




        // Send message
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = inputText.getText().toString().trim();

                if(message.isEmpty())
                {
                    return;
                }

                chatRoomModal.setLastMessageTimestamp(Timestamp.now());
                chatRoomModal.setLastMessageSenderId(UserModal.getCurrentUser().getEmail());
                // Updating Chat room
                FirebaseFirestore.getInstance().collection("ChatRooms").document(chatRoomId).set(chatRoomModal);

                ChatMessageModal chatMessageModal = new ChatMessageModal(message, UserModal.getCurrentUser().getEmail(), Timestamp.now());

                // Adding Messages
                FirebaseFirestore.getInstance().collection("ChatRooms").document(chatRoomId).collection("Chats")
                        .add(chatMessageModal)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful())
                                {
                                    // Clear input
                                    inputText.setText("");
                                }
                            }
                        });

            }
        });
    }

    // Setting for optimization
    @Override
    public void onStart() {
        super.onStart();
        if(chatMessagesAdapter !=null)
        {
            chatMessagesAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(chatMessagesAdapter !=null)
        {
            chatMessagesAdapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(chatMessagesAdapter !=null)
        {
            chatMessagesAdapter.startListening();
        }
    }
}
