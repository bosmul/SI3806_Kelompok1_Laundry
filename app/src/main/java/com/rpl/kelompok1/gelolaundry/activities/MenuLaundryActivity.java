package com.rpl.kelompok1.gelolaundry.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.rpl.kelompok1.gelolaundry.R;

public class MenuLaundryActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSignOut;
    private Button btnLihatOrder;
    private Button btnPesanKeluhan;
    private TextView update;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu_laundry);

        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        btnLihatOrder=(Button) findViewById(R.id.btnLihatOrder);
        btnPesanKeluhan = (Button) findViewById(R.id.btnPesanKeluhan);
        update = (TextView) findViewById(R.id.textViewProfile);
    }

    public void onClick(View view) {
        if (view == btnSignOut) {
            startActivity(new Intent(MenuLaundryActivity.this, LoginActivity.class));
        }
        if (view == btnLihatOrder) {
            startActivity(new Intent(MenuLaundryActivity.this, OrderListActivity.class));
        }
        if (view == btnPesanKeluhan) {
            startActivity(new Intent(MenuLaundryActivity.this, KeluhanListActivity.class));
        }
        if (view == update) {
            startActivity(new Intent(MenuLaundryActivity.this, LaundryListActivity.class));
        }
    }
}
