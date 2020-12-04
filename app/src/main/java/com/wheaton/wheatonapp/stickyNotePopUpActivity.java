package com.wheaton.wheatonapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class stickyNotePopUpActivity extends AppCompatActivity {

    public StickyNoteObject Sticky;
    List<StickyNoteObject> list;
    int p;
    String title;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_note_pop_up);

        Intent intent = getIntent();
        title = intent.getStringExtra("Title");
        Sticky = (StickyNoteObject) intent.getParcelableExtra("Sticky");
        p = intent.getIntExtra("P", -1);
        list = intent.getParcelableArrayListExtra("List");
        message = Sticky.getMsg();


        TextView t = (TextView) findViewById(R.id.textViewTitle);
        t.setText(Sticky.getTitle());
        TextView m = (TextView) findViewById(R.id.textViewMsg);
        m.setText(Sticky.getMsg());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);




    }

    //Sticky Note Pop Up Functions
    public void Close(View view) {
        finish();
    }


    public void saveButtonF(View view) {
        EditText tE = (EditText) findViewById(R.id.textViewTitle);
        EditText mE = (EditText) findViewById(R.id.textViewMsg);
        Sticky.setTitle(tE.getText().toString());
        Sticky.setMsg(mE.getText().toString());
        list.set(p, Sticky);
        list.remove(p);
    }




}