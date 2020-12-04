package com.wheaton.wheatonapp;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class customWeb extends WebViewClient {

    recylerAdapter adapterMain;
    ArrayList<StickyNoteObject> source;
    LinearLayoutManager HorizontalLayoutMain;
    RecyclerView recyclerViewMain;
    MainActivity Main;
    String currURL;

    //You can use pushData, pullData, and deleteData by calling like this: 'Main.pullData()'
    //Try ArrayList<Map<String, Object>> arrayName = Main.pullData() to create an arraylist
    //and add all Document objects to it from firestore. Document objects contain all data
    //for a note including url, text, name, associated user id, and timestamp.
    //Use arrayName.get(INDEX).getData() to retrieve the data. Check the Logcat logs to see
    //what this data looks like. It is logged everytime pullData is called.

    public customWeb(recylerAdapter adapter, LinearLayoutManager HorizontalLayout, RecyclerView recyclerView, MainActivity m) {
        super();
        adapterMain = adapter;
        HorizontalLayoutMain = HorizontalLayout;
        recyclerViewMain = recyclerView;
        Main = m;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        currURL = url;
        AddItemsToRecyclerViewArrayList();
        if (source.isEmpty()){
            source.add(new StickyNoteObject("EMPTY", "EMPTY"));
            Main.findViewById(R.id.recyclerview).setVisibility(View.INVISIBLE);
        }

        adapterMain = new recylerAdapter(source);
        HorizontalLayoutMain = new LinearLayoutManager(Main, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMain.setLayoutManager(HorizontalLayoutMain);
        recyclerViewMain.setAdapter(adapterMain);
    }

    public void AddItemsToRecyclerViewArrayList() {
        // Adding items to ArrayList
        source = new ArrayList<>();
        final ArrayList<DocumentSnapshot> noteContent = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("notes")
                .whereEqualTo("id", Main.myId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("firestore", document.getId() + " => " + document.getData());
                                noteContent.add(document);

                                //Change this to a call to some main function
                                source.add(new StickyNoteObject((String) document.get("name"), (String) document.get("text"), document.getId(), (String) document.get("url")));
                            }
                        } else {
                            Log.d("firestore", "Error getting documents: ", task.getException());
                        }

                    }
                });

    }
}

