package com.rpl.kelompok1.gelolaundry.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rpl.kelompok1.gelolaundry.R;
import com.rpl.kelompok1.gelolaundry.adapters.LaundryAdapter;
import com.rpl.kelompok1.gelolaundry.models.Laundry;

import java.util.ArrayList;
import java.util.List;

public class LaundryListActivity extends AppCompatActivity {
    private ListView listViewLaundry;
    private List<Laundry> listLaundry;
    private LaundryAdapter mLaundryAdapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    String nama, alamat, telepon;
    String newAlamat = "Enter Alamat";

    FirebaseUser user;
    Query query;

    @Override
    protected void onStart() {
        super.onStart();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listLaundry.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Laundry laundry = postSnapshot.getValue(Laundry.class);
                    listLaundry.add(laundry);

                    nama = laundry.getName();
                    alamat = laundry.getAlamat();
                    telepon = laundry.getTelepon();
                }

                mLaundryAdapter = new LaundryAdapter(LaundryListActivity.this, listLaundry);
                listViewLaundry.setAdapter(mLaundryAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_list);
        getSupportActionBar().hide();

        listViewLaundry = (ListView) findViewById(R.id.listViewLaundry);
        listLaundry = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference("laundry");
        query = mDatabase.orderByChild("id").equalTo(user.getUid());



        listViewLaundry.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Laundry laundry=listLaundry.get(i);
                showUpdateDialog(laundry.getId(), laundry.getEmail());
                return true;
            }
        });
    }



    private boolean updateLaundry(String id, String name, String email, String alamat, String telepon) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("laundry").child(id);

        Laundry laundry= new Laundry(id, name, email, alamat, telepon);
        dR.setValue(laundry);
        Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                newAlamat=data.getStringExtra("alamat");

            }
        }
    }

    private void showUpdateDialog(final String id, final String email) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_profile_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextNama = (EditText) dialogView.findViewById(R.id.editTextNama);
        final EditText editTextTelepon = (EditText) dialogView.findViewById(R.id.editTextTelepon);
        final TextView textViewAlamat = (TextView) dialogView.findViewById(R.id.textViewAlamat);

        textViewAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LaundryListActivity.this, MapsActivity.class), 1);
            }
        });

        textViewAlamat.setHint(newAlamat);
        editTextNama.setText(nama);
        editTextTelepon.setText(telepon);

        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate);

        dialogBuilder.setTitle(id);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = editTextNama.getText().toString().trim();
                String telepon = editTextTelepon.getText().toString().trim();
/*
                String alamat = textViewAlamat.getText().toString().trim();
*/
                if (!TextUtils.isEmpty(nama)) {
                    updateLaundry(id, nama, email, newAlamat, telepon);
                    b.dismiss();
                }
            }
        });
    }
}
