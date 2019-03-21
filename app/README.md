# **Sample App** (Using *Firebase Recycler Pagination* Library)
Here is sample app demonstrating features of `FirebaseRecyclerPagination` library.
Output will be as following GIF.

![](screengif.gif)

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

    //Android Paging Libray
    implementation "android.arch.paging:runtime:1.0.1"

    //Firebase Pagination Library
    implementation 'com.shreyaspatil:FirebaseRecyclerPagination:0.7-dev'
}
```
### App Setup
In this app, you are showing paginated list of Posts. Posts will load in `RecyclerView`
#### Data Model Class (Post.class)
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

    FirebaseRecyclerPagingAdapter<Post, PostViewHolder> mAdapter;
```

#### Initialization
```java
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
FirebasePagingOptions<Post> options = new FirebasePagingOptions.Builder<Post>()
                .setLifecycleOwner(this)
                .setQuery(mDatabase, config, Post.class)
                .build();
```
#### Init Adapter
`FirebaseRecyclerPagingAdapter` is built on the top of Android Architecture Components - Paging Support Library.
To implement, you should already have `RecyclerView.ViewHolder` subclass. Here We used `PostViewHolder` class. <br>
You can obtain key of Data model using `key`.

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
                                         @NonNull String key,
                                         @NotNull Post model) {

                holder.setItem(model);
            }
        };
```

#### Set Adapter
Finally, Set adapter to RecyclerView.
```java
        mRecyclerView.setAdapter(mAdapter);
```

#### Listener for Paging events
This is optional. After setting up the StateChangedListener it will respond to changes in RecyclerView events.
```java
        mAdapter.setStateChangedListener(new StateChangedListener() {
               @Override
               public void onInitLoading() {
                   //First Time Loading. Do Animation
               }

               @Override
               public void onLoading() {
                   //When Loading Every Time. Do Animation
               }

               @Override
               public void onLoaded() {
                   //When Items are loaded in RecyclerView
               }

               @Override
               public void onFinished() {
                   //When Items are fully loaded. List Ends.
               }

               @Override
               public void onError(DatabaseError databaseError) {
                   //When Error is Occured.
                   databaseError.toException().printStackTrace();
               }
           });
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
Thank You !
