package com.example.schoolteacher;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.schoolteacher.Model.Contacts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PeopleActivity extends AppCompatActivity {


    ImageButton addParentBtn;
    String noteId;

    FirebaseDatabase firebaseDatabase;
    String userID;
    String userIds;

    private RecyclerView peopleList;
    private FirebaseAuth mAuth;
    private DatabaseReference contactsClassRef, usersRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        noteId = getIntent().getStringExtra("id");

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.people);


        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                     case R.id.streamClass:
                         Intent streamIntent = new Intent(getApplicationContext(), StreamActivity.class);
                         streamIntent.putExtra("id",noteId );
                         streamIntent.putExtra("userIds", userIds);
                         startActivity(streamIntent);
                            break;

                    case R.id.classwork:
                        Intent intent = new Intent(getApplicationContext(), ClassworkActivity.class);
                        intent.putExtra("id",noteId );
                        startActivity(intent);
                        break;

                    case R.id.people:
                        break;
                }
                return true;
            }
        });



        addParentBtn = findViewById(R.id.studentBTN);
        addParentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent parentIntent = new Intent(getApplicationContext(), FindParentActivity.class);
                parentIntent.putExtra("id",noteId );
                startActivity(parentIntent);
                overridePendingTransition(0, 0);
            }
        });

        // start

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        contactsClassRef = FirebaseDatabase.getInstance().getReference().child("Class Contacts");

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        peopleList = findViewById(R.id.peoplerv);
        peopleList.setLayoutManager(new LinearLayoutManager(PeopleActivity.this));



    }



    // start message activ
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(contactsClassRef, Contacts.class)
                .build();


        FirebaseRecyclerAdapter<Contacts, PeopleActivity.PeopleViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, PeopleActivity.PeopleViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final PeopleViewHolder holder, int position, @NonNull Contacts model) {
                        final String userIds = getRef(position).getKey();
                        final String [] profileImage = {"default"};
                        usersRef.child(userIds).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("image")){
                                    profileImage[0] = dataSnapshot.child("image").getValue().toString();
                                    Picasso.get().load(profileImage[0]).placeholder(R.drawable.avatar).into(holder.profileImage);
                                }

                                final String userName = dataSnapshot.child("name").getValue().toString();
                                final String status = dataSnapshot.child("status").getValue().toString();
                                holder.userName.setText(userName);
                                holder.userStatus.setText(status);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public PeopleActivity.PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.people_display_layout, viewGroup, false);
                        return new PeopleActivity.PeopleViewHolder(view);
                    }
                };
        peopleList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class PeopleViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileImage;
        TextView userStatus, userName;

        public PeopleViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            userStatus = itemView.findViewById(R.id.users_status);
            userName = itemView.findViewById(R.id.users_profile_name);
        }
    }
















    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}