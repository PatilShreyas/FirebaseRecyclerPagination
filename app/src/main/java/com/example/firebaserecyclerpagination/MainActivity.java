package com.example.firebaserecyclerpagination;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagination;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPaginationAdapter;
import com.shreyaspatil.firebase.recyclerpagination.listener.FirebaseRecyclerPaginationListener;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;

    FirebaseRecyclerPaginationAdapter<String, ItemViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("list");

        FirebaseRecyclerPagination<String> mPagination = new FirebaseRecyclerPagination<>(
                this,
                mRecyclerView,
                String.class,       //Class of data
                mDatabase,          //Database Query
                FirebaseRecyclerPagination.ORDER_ASCENDING,     //Order of data
                15,     //Initial Item ViewHolders to load
                5       //Next Items to load after end of list
        );

        mAdapter = new FirebaseRecyclerPaginationAdapter<String, ItemViewHolder>(this) {
            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false));
            }

            @Override
            public void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int position, String model) {
                viewHolder.setItem(model);
            }
        };

        //Listener for callback events
        mPagination.setFirebaseRecyclerPaginationListener(new FirebaseRecyclerPaginationListener() {
            @Override
            public void onLoading() {
                //Do your loading animation
            }

            @Override
            public void onLoaded() {
                //After items loaded in RecyclerView
            }
        });

        //Set adapter to RecyclerView
        mPagination.setPaginationAdapter(mAdapter);
    }
}
