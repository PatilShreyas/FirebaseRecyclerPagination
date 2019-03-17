# Firebase Recycler Pagination

<a href='https://bintray.com/patilshreyas/maven/FirebaseRecyclerPagination/_latestVersion'><img src='https://api.bintray.com/packages/patilshreyas/maven/FirebaseRecyclerPagination/images/download.svg'></a>

Here is Android library to implement Pagination of Firebase Realtime Database in RecyclerView.

## Things To Do
- [X] Pagination for Up-Down List.
- [ ] Pagination for Down-Up (Reversed) List.
- [ ] Implementation Using Android Architecture Components.
- [ ] Realtime Support.

## Getting Started

### Maven
```maven
    repositories {
        jcenter()
    }
```
### Gradle
```gradle
dependencies {
    implementation 'com.shreyaspatil:FirebaseRecyclerPagination:0.1-beta'
}
```
### App

#### Declarations
- INIT_ITEMS_LOAD_COUNT is a value to load number of ViewHolders in RecyclerView initially.
- NEXT_ITEMS_LOAD_COUNT is a value which will load number of ViewHolders in RecyclerView after end of previous loaded items.
```java
public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    
    public final int INIT_ITEMS_LOAD_COUNT = 15;
    public final int NEXT_ITEMS_LOAD_COUNT = 10;

    FirebaseRecyclerPaginationAdapter<String, ItemViewHolder> mAdapter;
    ....
```
#### Init
```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        //Initialization of Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("list");
```

#### Initialization of FirebaseRecyclerPagination  
```java
FirebaseRecyclerPagination<String> mPagination = new FirebaseRecyclerPagination<>(
                this,
                mRecyclerView,      //RecyclerView
                String.class,       //Class of data
                mDatabase,          //Database Query
                FirebaseRecyclerPagination.ORDER_ASCENDING,     //Order of data
                INIT_ITEMS_LOAD_COUNT,  
                NEXT_ITEMS_LOAD_COUNT       
        );
```

#### Init Adapter
```java
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
```

#### Set Adapter
Finally, Set mAdapter to mPagination.
```java
        mPagination.setPaginationAdapter(mAdapter);
```

#### Listener for Callback events
- onLoading() - Will invoked every time when data is to be loading in RecyclerView.
- onLoaded() - Will Invoked every time when data is successfully loaded in RecyclerView.
```java
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
```
- Thus, we have implemented Firebase Recycler Pagination.
- Here is Complete Java Activity below.
## MainActivity.java
```java
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
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.listener.StateChangedListener;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    public final int INIT_ITEMS_LOAD_COUNT = 15;
    public final int NEXT_ITEMS_LOAD_COUNT = 10;

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
                INIT_ITEMS_LOAD_COUNT,     //Initial Item ViewHolders to load
                NEXT_ITEMS_LOAD_COUNT       //Next Items to load after end of list
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
```
