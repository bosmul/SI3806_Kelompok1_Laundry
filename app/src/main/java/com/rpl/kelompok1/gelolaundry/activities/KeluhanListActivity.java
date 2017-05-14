package com.rpl.kelompok1.gelolaundry.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rpl.kelompok1.gelolaundry.R;
import com.rpl.kelompok1.gelolaundry.adapters.KeluhanAdapter;
import com.rpl.kelompok1.gelolaundry.models.Keluhan;

import java.util.ArrayList;
import java.util.List;

public class KeluhanListActivity extends AppCompatActivity {
    private TextView textViewName;
    private ListView listViewKeluhan;
    private List<Keluhan> listKeluhan;
    private KeluhanAdapter mKeluhanAdapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    Query query;
    FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listKeluhan.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Keluhan keluhan = postSnapshot.getValue(Keluhan.class);
                    listKeluhan.add(keluhan);
                }
                mKeluhanAdapter = new KeluhanAdapter(KeluhanListActivity.this, listKeluhan);
                listViewKeluhan.setAdapter(mKeluhanAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keluhan_list);

        textViewName = (TextView) findViewById(R.id.textViewName);
        listViewKeluhan = (ListView) findViewById(R.id.listViewKeluhan);
        listKeluhan = new ArrayList<>();

        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewName.setText(emailFromIntent);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("keluhan");
        user = firebaseAuth.getCurrentUser();
        query =  mDatabase.orderByChild("idLaundry").equalTo(user.getUid());

        listViewKeluhan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Keluhan keluhan = listKeluhan.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), LihatKeluhanActivity.class);
                String namaUser = keluhan.getNamaUser();
                String nomorUser = keluhan.getNomorUser();
                String isi = keluhan.getIsi();
                String namaLaundry = keluhan.getNamaLaundry();
                String nomorLaundry = keluhan.getNomorLaundry();
                String idUser = keluhan.getIdUser();
                String idLaundry = keluhan.getIdLaundry();
                String idKeluhan = keluhan.getIdKeluhan();


                intent.putExtra("namaUser", namaUser);
                intent.putExtra("nomorUser", nomorUser);
                intent.putExtra("isi", isi);
                intent.putExtra("namaLaundry", namaLaundry);
                intent.putExtra("nomorLaundry", nomorLaundry);
                intent.putExtra("idUser", idUser);
                intent.putExtra("idLaundry", idLaundry);
                intent.putExtra("idKeluhan", idKeluhan);

                startActivity(intent);
            }
        });
    }
}