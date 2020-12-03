package com.wheaton.wheatonapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

        Button makeDraggableButton = findViewById(R.id.makeDraggable);
        makeDraggableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(v.getRootView());
            }
        });


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



    public void onButtonShowPopupWindowClick(View view) {
        Toast.makeText(this, "the fuck", Toast.LENGTH_SHORT).show();
        this.Close(view.getRootView());
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.draggablesticky, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            int dx;
            int dy;
            int xp;
            int yp;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dx = (int) (xp - event.getRawX());
                        dy = (int) (yp - event.getRawY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        xp = (int) (event.getRawX() + dx);
                        yp = (int) (event.getRawY() + dy);
                        popupWindow.update(xp, yp, -1, -1);
                        break;
                }
                return true;
            }
        });
    }
}