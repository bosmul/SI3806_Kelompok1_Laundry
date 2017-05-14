package com.rpl.kelompok1.gelolaundry.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rpl.kelompok1.gelolaundry.R;
import com.rpl.kelompok1.gelolaundry.models.Keluhan;
import com.rpl.kelompok1.gelolaundry.models.User;

import java.util.ArrayList;
import java.util.List;

public class LihatKeluhanActivity extends AppCompatActivity implements View.OnClickListener{
    EditText feedbackET;
    TextView userTV, laundry, isiKeluhan;
    Button kirim;
    private List<Keluhan> listKeluhan;
    private List<User> listUser;

    FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    String idKeluhan, idLaundry,idUser, namaUser, namaLaundry,
            nomorUser, nomorLaundry, isi, feedback;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_keluhan);

        feedbackET = (EditText) findViewById(R.id.editTextFeedback);

        userTV = (TextView) findViewById(R.id.textViewUser);
        isiKeluhan = (TextView) findViewById(R.id.textViewIsiKeluhan);
        kirim = (Button) findViewById(R.id.btnKirim);
        kirim.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference("keluhan");

        listKeluhan = new ArrayList<>();
        listUser = new ArrayList<>();

        idKeluhan = getIntent().getStringExtra("idKeluhan");
        idLaundry = getIntent().getStringExtra("idLaundry");
        idUser = getIntent().getStringExtra("idUser");
        namaLaundry = getIntent().getStringExtra("namaLaundry");
        namaUser = getIntent().getStringExtra("namaUser");
        nomorLaundry = getIntent().getStringExtra("nomorLaundry");
        nomorUser = getIntent().getStringExtra("nomorUser");
        isi = getIntent().getStringExtra("isi");

        userTV.setText(namaUser);
        isiKeluhan.setText(isi);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnKirim:
                updateKeluhan();
                finish();
                break;
        }
    }

    private boolean updateKeluhan() {
        DatabaseReference dR = mDatabase.child(idKeluhan);
        feedback = feedbackET.getText().toString().trim();

        Keluhan keluhan = new Keluhan(idKeluhan, idUser, idLaundry, namaUser, namaLaundry,
                nomorUser, nomorLaundry, isi, feedback);

        dR.setValue(keluhan);
        Toast.makeText(getApplicationContext(), "Feedback terkirim", Toast.LENGTH_LONG).show();
        return true;
    }
}
