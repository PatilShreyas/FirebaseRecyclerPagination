# **Sample App** (Using *Firebase Recycler Pagination* Library)
Here is sample app demonstrating features of `FirebaseRecyclerPagination` library.
Output will be as following GIF.

<img src="screengif.gif" height="500">

## Getting Started

### Maven Setup
```maven
    repositories {
        jcenter()
    }
```
### Gradle Setup
```groovy
dependencies {

    //RecyclerView
    implementation 'com.android.support:recyclerview-v7:28.0.0'

    //Firebase Database
    implementation 'com.google.firebase:firebase-database:16.1.0'
    implementation 'com.google.firebase:firebase-core:16.0.7'
    
    //Firebase-UI Library
    implementation 'com.firebaseui:firebase-ui-database:4.3.1'

    //Android Paging Libray
    implementation "android.arch.paging:runtime:1.0.1"

    //Firebase Pagination Library
    implementation 'com.shreyaspatil:FirebaseRecyclerPagination:0.7.2'
}
```
### App Setup
In this app, you are showing paginated list of Posts. Posts will load in `RecyclerView`
#### Data Model Class (Post.java)
```java
public class Post {
    public String title;
    public String body;

    public Post(){}

    public Post(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
```

### `MainActivity.java`

#### Declarations
```java
public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    FirebaseRecyclerPagingAdapter<Post, PostViewHolder> mAdapter;
```

#### Initialization
```java
   @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);

           mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
 
           //Initialize RecyclerView
           mRecyclerView = findViewById(R.id.recycler_view);
           mRecyclerView.setHasFixedSize(true);

           LinearLayoutManager mManager = new LinearLayoutManager(this);
           mRecyclerView.setLayoutManager(mManager);

           //Initialize Database
           mDatabase = FirebaseDatabase.getInstance().getReference().child("posts");

```
Dont Forgot to set `LayoutManager` to the RecyclerView.<br>
Set it using `RecyclerView#setLayoutManager()`

#### Setup Configuration for PagedList
First of all configure PagedList <br>
*Remember that, the size you will pass to `setPageSize()` method will load x3 items of that size.*
```java
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();
```

Then Configure Adapter by building FirebasePagingOptions. It will generic. <br>
*Remember one thing, don't pass Query with `orderByKey()`, `limitToFirst()` or `limitToLast()`. This will cause an error.*
```java
 DatabasePagingOptions<Post> options = new DatabasePagingOptions.Builder<Post>()
                .setLifecycleOwner(this)
                .setQuery(mDatabase, config, Post.class)
                .build();
```
#### Init Adapter
`FirebaseRecyclerPagingAdapter` is built on the top of Android Architecture Components - Paging Support Library.
To implement, you should already have `RecyclerView.ViewHolder` subclass. Here We used `PostViewHolder` class.

```java
        mAdapter = new FirebaseRecyclerPagingAdapter<Post, PostViewHolder>(options) {
            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder,
                                         int position,
                                         @NonNull Post model) {

                holder.setItem(model);
            }
            
            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        mSwipeRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
        };
```

#### Get Child Reference
To get reference of child from list, `FirebaseRecyclerPagingAdapter` has method called `getRef()`. You can obtain `DatabaseReference` of child using it. <br>
Get it using `FirebaseRecyclerPagingAdapter#getRef()`
For e.g.
```java
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder,
                                         int position,
                                         @NonNull Post model) {
                
                DatabaseReference reference = getRef(position);
            }
```
#### Error Handling
To get to know about `DatabaseError` caught during Paging, Override `onError()` method in adapter.
```java
            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                mSwipeRefreshLayout.setRefreshing(false);
                databaseError.toException().printStackTrace();
                // Handle Error
              
            }
```
#### Retrying List (After Error / Failure)
To retry items loading in RecyclerView, `retry()` method from Adapter class is used. <br>
Use it as `FirebaseRecyclerPagingAdapter#retry()`. <br>
This method should used only after caught in Error. `retry()` should not be invoked anytime other than ERROR state. <br>
Whenever `LoadingState` becomes `LoadingState.ERROR` we can use `retry()` to load items in RecyclerView which were unable to load due to recent failure/error and to maintain Paging List stable.<br>
See demo for method.

```java
        @Override
        protected void onError(@NonNull DatabaseError databaseError) {
            retry();          
        }
```

Or outside `FirebaseRecyclerPagingAdapter`

```java
        mAdapter.retry();
```

#### Refreshing List
To refresh items in RecyclerView, `refresh()` method from Adapter class is used. <br>
Use it as `FirebaseRecyclerPagingAdapter#refresh()`. <br>
This method clears all the items in RecyclerView and reloads the data again from beginning. <br>
See demo for method.

```java
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.refresh();
            }
        });
```

#### Set Adapter
Finally, Set adapter to RecyclerView.
```java
        mRecyclerView.setAdapter(mAdapter);
```


#### Lifecycle
At last, To begin populating data, call `startListening()` method. `stopListening()` stops the data being loaded.
```java
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
```
Thus, we have implemented Firebase Recycler Pagination.
***Thank You !***
