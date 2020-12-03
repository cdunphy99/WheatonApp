package com.wheaton.wheatonapp;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FragmentPopUp extends Fragment {

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
            Sticky.setMsg(mE.getText().toString());
            list.set(p,Sticky);

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

    @Nullable
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




        return rootView;
    }

    //Sticky Note Pop Up Functions
    public void Close(View view){
    }



}
