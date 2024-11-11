package com.example.finalprojectmobile2.util;

import android.util.Log;

import com.example.finalprojectmobile2.model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FirebaseUtil {

    // Getting user's reference
    public static DocumentReference getUserDocumentReference(String userId)
    {
        return FirebaseFirestore.getInstance().collection("users").document(userId);
    }
    // Getting all users' references
    public static CollectionReference allUserCollectionReference()
    {
        return FirebaseFirestore.getInstance().collection("users");
    }
    // Getting an object with the data from a user reference
    public static void getObjectUser(String userId)
    {
//        final UserModel resultModel[] = new UserModel[0];

        DocumentReference docRef = getUserDocumentReference(userId);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("HERE= "+documentSnapshot.toObject(UserModel.class).getUsername(), "test");
                callback
            }
        });


//       return resultModel[0];

    }
}
