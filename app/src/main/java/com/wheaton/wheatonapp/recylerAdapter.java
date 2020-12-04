package com.wheaton.wheatonapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// The adapter class which
// extends RecyclerView Adapter
public class recylerAdapter extends RecyclerView.Adapter<recylerAdapter.MyView> {

    // List with String type
    private List<StickyNoteObject> list;
    Context context;
    ViewGroup parent;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView textView;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view)
        {
            super(view);

            // initialise TextView with id
            textView = (TextView)view
                    .findViewById(R.id.textview);
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public recylerAdapter(List<StickyNoteObject> horizontalList)
    {
        this.list = horizontalList;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parentin,
                                     int viewType)
    {
        // Inflate item.xml using LayoutInflator
        View itemView = LayoutInflater.from(parentin.getContext()).inflate(R.layout.sticky_card_view, parentin, false);
        parent = parentin;


        // return itemView
        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        context = holder.itemView.getContext();

        // Set the text of each item of
        // Recycler view with the list items
        holder.textView.setText(list.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StickyNoteObject stickyOut = (StickyNoteObject) list.get(position);
                /*Log.d("cardView", "Yo " + stickyOut.getTitle());
                Intent i = new Intent(context, stickyNotePopUpActivity.class);
                i.putExtra("Title", stickyOut.getTitle());
                i.putExtra("Sticky", stickyOut);
                i.putExtra("P",position);
                i.putParcelableArrayListExtra("List", (ArrayList<StickyNoteObject>) list);

                context.startActivity(i);*/

                //notifyDataSetChanged();

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.findViewById(R.id.webView).setFocusable(false);
                activity.findViewById(R.id.webView).setFocusableInTouchMode(false);
                FragmentPopUp fragment = new FragmentPopUp(stickyOut, list, position, activity, recylerAdapter.this);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.stickyView, fragment).addToBackStack(null).commit();


            }
        });
    }


    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return list.size();
    }
}