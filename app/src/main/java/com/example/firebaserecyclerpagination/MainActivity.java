package com.example.firebaserecyclerpagination;

import android.app.Dialog;
import android.arch.paging.PagedList;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.shreyaspatil.firebase.recyclerpagination.FirebasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.listener.StateChangedListener;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;

    FirebaseRecyclerPagingAdapter<Post, PostViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize RecyclerView
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);

        //Initialize Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("posts");

        //Initialize PagedList Configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();

        //Initialize FirebasePagingOptions
        FirebasePagingOptions<Post> options = new FirebasePagingOptions.Builder<Post>()
                .setLifecycleOwner(this)
                .setQuery(mDatabase, config, Post.class)
                .build();

        //Initialize Adapter
        mAdapter = new FirebaseRecyclerPagingAdapter<Post, PostViewHolder>(options) {
            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder,
                                            int position,
                                            @NonNull String key,
                                            @NotNull Post model) {
                holder.setItem(model);
            }
        };

        //Set Adapter to RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        //Paging Event Listener for State of RecyclerView
        mAdapter.setStateChangedListener(new StateChangedListener() {
            @Override
            public void onInitLoading() {

            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded() {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onError(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });

        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPostDialog.show(MainActivity.this);
            }
        });

    }

    //Start Listening Adapter
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    //Stop Listening Adapter
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    //Dialog to add new posts
    static class AddPostDialog{

        public static void show(Context mContext){
            final Dialog mDialog = new Dialog(mContext);
            mDialog.setContentView(R.layout.dialog_add_layout);
            final EditText mTitleEditText = mDialog.findViewById(R.id.editTextTitle);
            final EditText mBodyEditText = mDialog.findViewById(R.id.editTextBody);
            Button mAddPostButton = mDialog.findViewById(R.id.buttonAddPost);
            Button mExitButton = mDialog.findViewById(R.id.buttonExit);

            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("posts");

            mAddPostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = mTitleEditText.getText().toString();
                    String body = mBodyEditText.getText().toString();

                    Post post = new Post(title,body);

                    mDatabase.push().setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mTitleEditText.setText("");
                            mBodyEditText.setText("");
                        }
                    });
                }
            });

            mExitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

            //Finally, Show the dialog
            mDialog.show();
        }


    }
}
