package com.wheaton.wheatonapp;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class customWeb extends WebViewClient {

    recylerAdapter adapterMain;
    ArrayList<StickyNoteObject> source;
    LinearLayoutManager HorizontalLayoutMain;
    RecyclerView recyclerViewMain;
    MainActivity Main;
    String currURL;

    //You can use pushData, pullData, and deleteData by calling like this: 'Main.pullData()'

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

    public void AddItemsToRecyclerViewArrayList() {
        // Adding items to ArrayList
        source = new ArrayList<>();
        source.add(new StickyNoteObject("StickyNEW1", "Hello1"));
        source.add(new StickyNoteObject("StickyNEW2", "Hello2"));
        source.add(new StickyNoteObject("StickyNEW3", "Hello3"));
        source.add(new StickyNoteObject("StickyNEW3", currURL));
    }
}

