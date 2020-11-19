package com.wheaton.wheatonapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private WebView webView;
    private boolean cardView_Shown = true;

    RecyclerView recyclerView;
    ArrayList<String> source;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    recylerAdapter adapter;
    LinearLayoutManager HorizontalLayout;

    View ChildView;
    int RecyclerViewItemPosition;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // FAB code
        FloatingActionButton mainFAB = findViewById(R.id.mainFAB);
        View view;
        final FloatingActionButton miniFAB1 = findViewById(R.id.miniFAB1);
        final FloatingActionButton miniFAB2 = findViewById(R.id.miniFAB2);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.google.com");

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

        //https://www.geeksforgeeks.org/android-horizontal-recyclerview-with-examples/
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
        AddItemsToRecyclerViewArrayList();
        adapter = new recylerAdapter(source);
        HorizontalLayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(adapter);


    }

    public void AddItemsToRecyclerViewArrayList() {
        // Adding items to ArrayList
        source = new ArrayList<>();
        source.add("Sticky1");
        source.add("Sticky2");
        source.add("Sticky3");
    }


    public void click(View view) {
        TextView t = (TextView) findViewById (R.id.textview);
        Toast.makeText(getApplicationContext(), "You have pressed a sticky.".concat(t.getText().toString()), Toast.LENGTH_SHORT).show();
    }

    public void hide_showCardView(){
        RelativeLayout r = (RelativeLayout) findViewById(R.id.rView);
        if(cardView_Shown){
            r.setVisibility(View.GONE);
            cardView_Shown = false;
        } else {
            r.setVisibility(View.VISIBLE);
            cardView_Shown = true;
        }
    }

}