# Firebase Recycler Pagination

[ ![Download](https://api.bintray.com/packages/patilshreyas/maven/FirebaseRecyclerPagination/images/download.svg?version=0.5-beta) ](https://bintray.com/patilshreyas/maven/FirebaseRecyclerPagination/0.5-beta/link)

Here is Android library to implement Pagination of Firebase Realtime Database in RecyclerView.

## Things To Do
- [X] Pagination for Up-Down List.
- [X] Implementation Using Android Architecture Components.
- [ ] Pagination for Down-Up (Reversed) List.

## Sample App
The Sample app is available in [`app/`](app) directory that demonstrates feature of this library.

## Installation
FirebaseRecyclerPagingation Library binds Firebase Realtime Database Query to a RecyclerView by loading Data in pages. FirebaseRecyclerPagingAdapter is build ton top of Android Paging Support Library. Before using this adapter in your app, you will have to implement dependency on the support library.
```groovy
implementation 'android.arch.paging:runtime:1.x.x'
```

### Gradle
Open Build.gradle of your project.
```groovy
allprojects {
    repositories {
        google()
        jcenter()
    }
}
```
Open Build.gradle file of app module and then...
```groovy
dependencies {
    //Android Paging Libray
    implementation "android.arch.paging:runtime:1.0.1"
    
    //Firebase Pagination Library
    implementation 'com.shreyaspatil:FirebaseRecyclerPagination:0.5-beta'
}
```

## Contribution
We'll love to make it happen. Let's make library more perfect, powerful and useful for everyone!