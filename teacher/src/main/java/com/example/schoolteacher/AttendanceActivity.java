package com.example.schoolteacher;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.schoolteacher.Model.ClassModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AttendanceActivity extends AppCompatActivity {

    public static String time, period;
    ListView listView;
    ListAdapter adapter;

    ArrayAdapter<String> adapterSpinner;

    private ArrayList<String> names;
    private ArrayList<String> registers;
    private List<ClassModel> classes;

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        //toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // attendance

        time = getIntent().getStringExtra("DATE");
        period = getIntent().getStringExtra("PERIOD");

        Log.d("Attendance", time + " -- " + period);
        listView = findViewById(R.id.attendanceListViwe);

        names = new ArrayList<>();
        registers = new ArrayList<>();

        Button btn = findViewById(R.id.loadButton);
        assert btn != null;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadListView(v);
            }

            private void loadListView(View v) {
            }
        });

        Button btnx = findViewById(R.id.buttonSaveAttendance);
        assert btnx != null;

        btnx.setOnClickListener(v -> {
            Toast.makeText(getBaseContext(), "Saving", Toast.LENGTH_LONG).show();
//                adapter.saveAll();
        });

        initialize();
    }

    public void initialize() {

        classes = new ArrayList<>();

        spinner = findViewById(R.id.attendanceSpinner);
        adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapterSpinner);

        populateSpinner();
    }

    private void populateSpinner() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {

            //Add code for loading login activity
            return;
        }

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    ClassModel mClass = d.getValue(ClassModel.class);

                    if (mClass != null) {

                        if (mClass.getCreatedBy() != null && mClass.getCreatedBy().equals(user.getEmail())) {

                            classes.add(mClass);
                            adapterSpinner.add(mClass.getClassName());
                        }
                    }
                }

                adapterSpinner.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadListView(View view) {
        names.clear();
        registers.clear();
        /*
        String qu = "SELECT * FROM STUDENT WHERE cl = '" + spinner.getSelectedItem().toString() + "' " +
                "ORDER BY ROLL";
        Cursor cursor = AppBase.handler.execQuery(qu);
        if (cursor == null || cursor.getCount() == 0) {
            Log.e("Attendance", "Null cursor");
        } else {
            int ctr = 0;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                names.add(cursor.getString(0) + " (" + cursor.getInt(4) + ')');
                registers.add(cursor.getString(2));
                cursor.moveToNext();
                ctr++;
            }
            if (ctr == 0) {
                Toast.makeText(getBaseContext(), "No Students Found", Toast.LENGTH_LONG).show();
            }
            Log.d("Attendance", "Got " + ctr + " students");
        }
        adapter = new ListAdapter(thisActivity, names, registers);
        listView.setAdapter(adapter);

         */
    }


    // itemSelected toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
