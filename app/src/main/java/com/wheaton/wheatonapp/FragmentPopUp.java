package com.wheaton.wheatonapp;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentPopUp extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void editData(String docId, String name, String text) {
        if(!(docId == null)){
            db.collection("notes").document(docId).update("name", name, "text", text);
        }
        else{
            Log.d("firestore", "docId was blank in editData");
        }
    }

    public void editData(String name, String text) {
    }

    public StickyNoteObject Sticky;
    List<StickyNoteObject> list;
    View rootView;
    int p;
    AppCompatActivity activity;
    recylerAdapter adapter;



    public FragmentPopUp(StickyNoteObject StickyIn, List<StickyNoteObject> listIn, int pIn, AppCompatActivity activityIn, recylerAdapter r) {
        Sticky = StickyIn;
        list = listIn;
        p = pIn;
        activity= activityIn;
        adapter = r;
    }

    View.OnClickListener saveButtonListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            EditText tE = (EditText) rootView.findViewById(R.id.textViewTitle);
            EditText mE = (EditText) rootView.findViewById(R.id.textViewMsg);
            Sticky.setTitle(tE.getText().toString());
            Sticky.setMsg(tE.getText().toString());
            list.set(p,Sticky);

            Map<String, Object> note = new HashMap<>();

            Date date = new Date();
            Timestamp timeStamp = new Timestamp(date);

            note.put("name", tE.getText().toString());
            note.put("text", mE.getText().toString());
            note.put("time", timeStamp);
            note.put("id", MainActivity.myId);
            //note.put("url", cu);


            if(Sticky.getDocId() == null){
                db.collection("notes")
                        .add(note)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("firestore", "Note written with ID: " + documentReference.getId());
                                Sticky.setDocId(documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("firestore", "Error adding note to firestore", e);
                            }
                        });
            }
            else{
                editData(Sticky.getDocId(), tE.getText().toString(), mE.getText().toString());
            }

        }
    };

    View.OnClickListener closeButtonListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            activity.findViewById(R.id.webView).setFocusable(true);
            activity.findViewById(R.id.webView).setFocusableInTouchMode(true);
            FrameLayout f = (FrameLayout) activity.findViewById(R.id.stickyView);
            f.removeAllViews();
            adapter.notifyDataSetChanged();
        }
    };

    View.OnClickListener dragListener = new View.OnClickListener() {
        @Override
        public void onClick(View view){
            activity.findViewById(R.id.webView).setFocusable(true);
            activity.findViewById(R.id.webView).setFocusableInTouchMode(true);
            FrameLayout f = (FrameLayout) activity.findViewById(R.id.stickyView);
            f.removeAllViews();
            onButtonShowPopupWindowClick(view);

        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_sticky_note_pop_up, container, false);

        TextView t = (TextView) rootView.findViewById(R.id.textViewTitle);
        t.setText(Sticky.getTitle());
        TextView m = (TextView) rootView.findViewById(R.id.textViewMsg);
        m.setText(Sticky.getMsg());

        Button Save = (Button) rootView.findViewById(R.id.buttonSave);
        Save.setOnClickListener(saveButtonListener);
        Button Close = (Button) rootView.findViewById(R.id.buttonClose);
        Close.setOnClickListener(closeButtonListener);
        Button Draggable = rootView.findViewById(R.id.makeDraggable);
        Draggable.setOnClickListener(dragListener);



        return rootView;
    }

    //Sticky Note Pop Up Functions
    public void Close(View view){
    }
    public void onButtonShowPopupWindowClick(View view) {
        this.Close(view.getRootView());
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(getContext());
        final View popupView = inflater.inflate(R.layout.draggablesticky, null);
        TextView titleTV = popupView.findViewById(R.id.dragtitle);
        TextView messageTV = popupView.findViewById(R.id.dragmessage);
        titleTV.setText(Sticky.getTitle());
        messageTV.setText(Sticky.getMsg());

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
