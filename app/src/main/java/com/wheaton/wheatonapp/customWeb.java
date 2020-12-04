package com.wheaton.wheatonapp;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

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
        adapterMain = new recylerAdapter(source);
        HorizontalLayoutMain = new LinearLayoutManager(Main, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMain.setLayoutManager(HorizontalLayoutMain);
        recyclerViewMain.setAdapter(adapterMain);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        //Main.deleteData(); //get these from from source
        /*for (int i = 0; i < source.size(); i++){
            StickyNoteObject currSticky = source.get(i);
            Main.pushData(currSticky.getTitle(),currSticky.getMsg(),currURL);
        }*/
    }

    public void AddItemsToRecyclerViewArrayList() {
        // Adding items to ArrayList
        source = new ArrayList<>();
        ArrayList<Map<String, Object>> data = Main.pullData();
        for(int i = 0; i < data.size(); i++){
            data.get(i).get(currURL);
        }
        source.add(new StickyNoteObject("StickyNEW1", "Hello1"));
        source.add(new StickyNoteObject("StickyNEW2", "Hello2"));
        source.add(new StickyNoteObject("StickyNEW3", "Hello3"));
        source.add(new StickyNoteObject("StickyNEW3", currURL));
    }
}

