package com.wheaton.wheatonapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private WebView webView;
    private boolean cardView_Shown = true;
    private boolean addressBar_Shown = false;

    RecyclerView recyclerView;
    ArrayList<StickyNoteObject> source;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    recylerAdapter adapter;
    LinearLayoutManager HorizontalLayout;

    View ChildView;
    int RecyclerViewItemPosition;

    String myId = "1";

    public void pushData(String name, String text, String url){
        //Description:
        //This function pushes data to the Firebase database ("Cloud Firestore") in the form of
        //a Map containing the name of the note, the content or "text" of the note, and
        //an auto-generated timestamp of the current time. Firestore automatically creates
        //a unique ID for the note which can be used to recall or edit it directly.
        //All Firebase data can be found inside our Firebase project named "cantaloupe"
        //in our Google Firebase console page.
        //Usage:
        //pushdata("Grocery List", "eggs, ham, orange juice");

        Map<String, Object> note = new HashMap<>();

        Date date = new Date();
        Timestamp timeStamp = new Timestamp(date);

        note.put("name", name);
        note.put("text", text);
        note.put("time", timeStamp);
        note.put("id", myId);
        note.put("url", url);

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

    public void editData(String docId, String name, String text) {
        db.collection("notes").document(docId).update("name", name, "text", text);
    }

    boolean subFABsVisible = false;

    public ArrayList<DocumentSnapshot> pullData(){
        //Description:
        //This function pulls all notes from Firebase that have the the Id found in sharedPrefs.
        //SharedPrefs will contain this instance of the app's unique Id (and if it contains nothing
        //it will be updated to contain a random number that is not already used as an id in
        //Firebase. The function returns an array of Maps containing the Document data from firestore.
        //It will return null if an error occurs and will log the error with the tag 'firestore'.
        //Usage:
        //ArrayList<Map<String, Object>> arrayName = pullData();
        //or
        //if(pullData() != null){ CODE }

        Log.d("firestore", "Began firestore call.");

        final boolean[] found = {false};
        final ArrayList<DocumentSnapshot> noteContent = new ArrayList<>();

        db.collection("notes")
                .whereEqualTo("id", myId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("firestore", document.getId() + " => " + document.getData());
                                noteContent.add(document);
                            }
                            found[0] = true;
                        } else {
                            Log.d("firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });

        if(found[0]){
            return noteContent;
        }
        else return null;
    }

    public void deleteData(String documentId){
        //Description:
        //This function will take a Document ID, which can be found in the Document objects
        //in the array returned by pullData, and delete that particular document from the
        //firestore database. It does not return anything.
        //Usage:
        //deleteData("Document ID String")
        //Example:
        //deleteData("agzJJe3lh7GpUpgFYMwe")  <--- This is what Document IDs look like

        db.collection("notes").document(documentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("firestore", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("firestore", "Error deleting document", e);
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Main", "Yo ");

        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);

        myId = prefs.getString("wheaton_myId", "DEFAULT_VALUE");

        // FAB code
        FloatingActionButton mainFAB = findViewById(R.id.mainFAB);
        View view;
        final FloatingActionButton miniFAB1 = findViewById(R.id.miniFAB1);
        final FloatingActionButton miniFAB2 = findViewById(R.id.miniFAB2);

        String retId = prefs.getString("wheaton_myId", "none");
        Log.d("idWorks2", retId);

        if(retId.equals("none")){
            Log.d("idWorks", "equals");

            Random rand = new Random();
            String sender = String.valueOf(rand.nextInt(999999999));

            if(pullData() == null){
                sender = String.valueOf(rand.nextInt(999999999));
                Log.d("idWorksrandom", sender);
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("wheaton_myId", sender);
            editor.apply();

            String inId = prefs.getString("wheaton_myId", "none");
            Log.d("idWorks", inId);

            myId = inId;
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

        miniFAB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide_showCardView();
            }
        });
        miniFAB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleAddressBar();
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
        AddItemsToRecyclerViewArrayList();
        adapter = new recylerAdapter(source);
        HorizontalLayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(adapter);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new customWeb(adapter,HorizontalLayout,recyclerView,MainActivity.this));
        webView.loadUrl("https://www.google.com");





    }




    public void AddItemsToRecyclerViewArrayList() {
        // Adding items to ArrayList
        source = new ArrayList<>();

        db.collection("notes")
                .whereEqualTo("id", myId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("firestore", document.getId() + " => " + document.getData());
                                source.add(new StickyNoteObject((String) document.get("name"), (String) document.get("text"), document.getId(), (String) document.get("url")));
                            }
                        } else {
                            Log.d("firestore", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }


    public void click(View view) {
        TextView t = (TextView) findViewById (R.id.textview);
        Toast.makeText(getApplicationContext(), "You have pressed a sticky.".concat(t.getText().toString()), Toast.LENGTH_SHORT).show();
    }

    public void hide_showCardView(){
        RecyclerView rview = (RecyclerView) findViewById(R.id.recyclerview);
        Button addStickyButton = (Button) findViewById(R.id.addStickyButton);

        //LinearLayout LL = (LinearLayout) findViewById(R.id.rView);
        if(cardView_Shown){
            rview.setVisibility(View.GONE);
            addStickyButton.setVisibility(View.GONE);
            cardView_Shown = false;
        } else {
            if(addressBar_Shown){
                toggleAddressBar();
            }
            rview.setVisibility(View.VISIBLE);
            addStickyButton.setVisibility(View.VISIBLE);
            cardView_Shown = true;
        }
    }

    public void add(View view){
        source.add(new StickyNoteObject("New Sticky", "New Message"));
        if (source.get(0).getTitle().equals("EMPTY")){
            source.remove(1);
        }
        adapter.notifyDataSetChanged();
    }

    public void toggleAddressBar(){
        EditText addressBar = (EditText) findViewById(R.id.addressBar);
        Button goButton = (Button) findViewById(R.id.goButton);
        if(addressBar_Shown) {
            addressBar.setVisibility(View.GONE);
            goButton.setVisibility(View.GONE);
            addressBar_Shown = false;
        }
        else{
            if(cardView_Shown) {
                hide_showCardView();
            }
            addressBar.setVisibility(View.VISIBLE);
            goButton.setVisibility(View.VISIBLE);
            addressBar_Shown = true;
        }
    }

    public void goToURL(View view){
        EditText addressBar = (EditText) findViewById(R.id.addressBar);
        WebView web = (WebView) findViewById(R.id.webView);
        webView.loadUrl("https://www." + addressBar.getText().toString());
    }




}