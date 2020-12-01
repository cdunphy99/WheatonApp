package com.wheaton.wheatonapp;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DraggableSticky extends AppCompatActivity implements View.OnTouchListener {
    String title = "";
    String message = "";
    float dX;
    float dY;
    int lastAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("draggalbe", "drag");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draggablesticky);
        View draggableView = findViewById(R.id.draglayout);
        draggableView.setOnTouchListener(this);
        TextView draggableTitle = findViewById(R.id.dragtitle);
        TextView draggableMessage = findViewById(R.id.dragmessage);
        draggableTitle.setText(title);
        draggableMessage.setText(message);
    }

    public DraggableSticky(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                    Toast.makeText(DraggableSticky.this, "Clicked!", Toast.LENGTH_SHORT).show();
                break;

            default:
                return false;
        }
        return true;
    }
}
