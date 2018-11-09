package inc.elevati.imycity.utils;

import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.UUID;


public class FirebaseDatabaseSender implements UtilsInterface.DatabaseSender {

    @Override
    public void send(String title, String description, String fileName, DatabaseReference databaseReference) {
        String postId = UUID.randomUUID().toString();
        DatabaseReference postRef = databaseReference.child("Posts").child(postId);
        postRef.child("title").setValue(title);
        postRef.child("description").setValue(description);
        postRef.child("filename").setValue(fileName);
        postRef.child("data").setValue(Calendar.getInstance().getTime().toString());
    }
}
