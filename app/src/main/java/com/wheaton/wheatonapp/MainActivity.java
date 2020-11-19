package com.wheaton.wheatonapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String myId = "1";

    protected void pushData(String name, String text){
        //Description:
        //This function pushes data to the Firebase database ("Cloud Firestore") in the form of
        //a Map containing the name of the note, the content or "text" of the note, and
        //an auto-generated timestamp of the current time. Firestore automatically creates
        //a unique ID for the note which can be used to recall or edit it directly.
        //All Firebase data can be found inside our Firebase project named "cantaloupe"
        //in our Google Firebase console page.
        //Usage:
        //pushdata("Grocery List", "eggs, ham, orange juice");
        //May be changed later to store a json encoded string of data to support markdown.

        Map<String, Object> note = new HashMap<>();

        Date date = new Date();
        Timestamp timeStamp = new Timestamp(date);

        note.put("name", name);
        note.put("text", text);
        note.put("time", timeStamp);
        note.put("id", myId);

        db.collection("notes")
                .add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("firestore", "Note written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("firestore", "Error adding note to firestore", e);
                    }
                });
    }


    boolean subFABsVisible = false;

    protected boolean pullData(){
        //Description:
        //This function pulls all notes from Firebase that have the the Id found in sharedPrefs.
        //SharedPrefs will contain this instance of the app's unique Id (and if it contains nothing
        //it will be updated to contain a random number that is not already used as an id in
        //Firebase. For the time being the function simply prints each note to Logcat for testing
        //purposes, but it will be updated to send all notes to an array for display in the app's
        //main activity. This function returns a boolean that will be true when any notes are found
        //with this app's Id. The function is also used to test if an Id is already used in Firebase.
        //Usage:
        //pullData();
        //or
        //if(pullData()){}

        final boolean[] found = {false};

        db.collection("notes")
                .whereEqualTo("id", myId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("firestore", document.getId() + " => " + document.getData());
                            }
                            found[0] = true;
                        } else {
                            Log.d("firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });

        if(found[0]){
            return true;
        }
        else return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FAB code
        FloatingActionButton mainFAB = findViewById(R.id.mainFAB);
        View view;
        final FloatingActionButton miniFAB1 = findViewById(R.id.miniFAB1);
        final FloatingActionButton miniFAB2 = findViewById(R.id.miniFAB2);


        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);

        String retId = prefs.getString("wheaton_myId", "none");
        Log.d("idWorks", retId);

        if(retId.equals("none")){
            Log.d("idWorks", "equals");

            Random rand = new Random();
            String sender = String.valueOf(rand.nextInt(999999999));

            while(!pullData()){
                sender = String.valueOf(rand.nextInt(999999999));
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("wheaton_myId", sender);
            editor.apply();

            String inId = prefs.getString("wheaton_myId", "none");
            Log.d("idWorks", inId);

            myId = inId;
        }

        pullData();
    }


        mainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context;
                CharSequence text;
                Toast.makeText(getApplicationContext(), "You have pressed the FAB.", Toast.LENGTH_SHORT).show();
                // I will look into transitions to make this visibility change look "smooth" to the user. right now it should look instant.
                if(subFABsVisible) {
                    miniFAB1.hide();
                    miniFAB2.hide();
                    subFABsVisible = false;
                }
                else{
                    miniFAB1.show();
                    miniFAB2.show();
                    subFABsVisible = true;
                }
            }
        });
    }
}