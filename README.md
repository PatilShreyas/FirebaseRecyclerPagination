# **Firebase Recycler Pagination**

[![FirebaseOpensource.com](https://img.shields.io/badge/Docs-firebaseopensource.com-orange.svg)](
https://firebaseopensource.com/projects/patilshreyas/firebaserecyclerpagination/
)

Here is Android library to implement *Pagination* of Firebase Realtime Database in `RecyclerView`.

## Sample App
The Sample app is available in [`app/`](app) directory that demonstrates feature of this library.
Step by step description is given [`here`](app/README.md)

## Installation
FirebaseRecyclerPagingation Library binds Firebase Realtime Database Query to a `RecyclerView` by loading Data in pages. `FirebaseRecyclerPagingAdapter` is built on top of Android Paging Support Library. Before using this adapter in your app, you will have to implement dependency on the support library.
```groovy
implementation 'android.arch.paging:runtime:1.x.x'
```

### Gradle
Open [`Build.gradle`](build.gradle) of your project.
```groovy
allprojects {
    repositories {
        google()
        jcenter()
    }
}
```
Open [`Build.gradle`](app/build.gradle) file of `app` module and then..
```groovy
dependencies {
    //Android Paging Libray
    implementation "android.arch.paging:runtime:1.0.1"

    //Firebase Pagination Library
    implementation 'com.shreyaspatil:FirebaseRecyclerPagination:1.0.1'
}
```

## Contribution
We'll love to make it happen. Let's make library more perfect, powerful and useful for everyone!
