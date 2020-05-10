package com.bagas.tanganicovid_19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.bagas.tanganicovid_19.adapters.AdapterPosts;
import com.bagas.tanganicovid_19.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiskusiActivity extends AppCompatActivity {
    ActionBar actionBar;
    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;

    //FirebaseAuth
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diskusi);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Diskusi");

        firebaseAuth = FirebaseAuth.getInstance();

        //recycylerview and its properties
        recyclerView = findViewById(R.id.postRecylerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //show newest post first, for this load from list
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //setLayout to recyler view
        recyclerView.setLayoutManager(layoutManager);

        //init post list
        postList = new ArrayList<>();
        loadPost();
    }

    private void loadPost() {
        //path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        //get all data from posts
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    postList.add(modelPost);

                    //adapter
                    adapterPosts = new AdapterPosts(getApplicationContext(), postList);

                    //set adapter to recyclerView
                    recyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case error
                Toast.makeText(getApplicationContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchPost (final String searchQuery) {
        //path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        //get all data from posts
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    if(modelPost.getpTitle().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            modelPost.getpDescr().toLowerCase().contains(searchQuery.toLowerCase())) {
                                postList.add(modelPost);
                    }



                    //adapter
                    adapterPosts = new AdapterPosts(getApplicationContext(), postList);

                    //set adapter to recyclerView
                    recyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case error
                Toast.makeText(getApplicationContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_diskusi, menu);

        //searchView to search posts by post title / desc
       MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)MenuItemCompat.getActionView(item);

        //search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //called when user press search button
                if(!TextUtils.isEmpty(s)){
                    searchPost(s);
                } else {
                    loadPost();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //called when user press any letter
                if(!TextUtils.isEmpty(s)){
                    searchPost(s);
                } else {
                    loadPost();
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id  = item.getItemId();

        if(id == R.id.buat_postingan) {
            Intent intent = new Intent(DiskusiActivity.this, AddPostActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
