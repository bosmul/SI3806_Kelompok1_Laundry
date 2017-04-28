package com.rpl.kelompok1.gelolaundry.activities;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.rpl.kelompok1.gelolaundry.adapters.OrderAdapter;
import com.rpl.kelompok1.gelolaundry.models.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {
    private AppCompatActivity activity = OrderListActivity.this;
    private AppCompatTextView textViewName;
    private ListView listViewOrder;
    private List<Order> listOrder;
    private OrderAdapter mOrderAdapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    Query query, query2;
    FirebaseUser user;
    String berat, harga, parfurm, status;

    @Override
    protected void onStart() {
        super.onStart();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                listOrder.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Order order = postSnapshot.getValue(Order.class);
                    //adding artist to the list
                    listOrder.add(order);

                    berat = order.getBerat();
                    harga = order.getHarga();
                    parfurm = order.getParfurm();
                    status = order.getStatus();
                }
                //creating adapter
                mOrderAdapter = new OrderAdapter(OrderListActivity.this, listOrder);
                //attaching adapter to the listview
                listViewOrder.setAdapter(mOrderAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        listViewOrder = (ListView) findViewById(R.id.listViewOrder);
    }

    private void initObjects() {
        listOrder = new ArrayList<>();

        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewName.setText(emailFromIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        getSupportActionBar().hide();

        initViews();
        initObjects();
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("order");
        user = firebaseAuth.getCurrentUser();
        query =  mDatabase.orderByChild("idLaundry").equalTo(user.getUid());
        query2 = mDatabase.orderByChild("status").equalTo("Selesai");

        listViewOrder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Order order=listOrder.get(i);
                showUpdateDialog(order.getIdOrder(), order.getIdLaundry(), order.getIdUser(),
                        order.getNamaUser(), order.getNamaLaundry(), order.getAlamatLaundry(), order.getAlamatUser(), order.getTipe());
                return true;
            }
        });
    }

    private void showUpdateDialog(final String idOrder, final String idLaundry, final String idUser, final String namaUser,
                                        final String namaLaundry, final String alamatLaundry, final String alamatUser,
                                        final String tipe) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextBerat = (EditText) dialogView.findViewById(R.id.editTextBerat);
        final EditText editTextHarga = (EditText) dialogView.findViewById(R.id.editTextHarga);
        final EditText editTextParfurm = (EditText) dialogView.findViewById(R.id.editTextParfurm);

        editTextBerat.setText(berat);
        editTextHarga.setText(harga);
        editTextParfurm.setText(parfurm);

        final Spinner spinnerStatus = (Spinner) dialogView.findViewById(R.id.spinnerStatus);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateOrder);

        dialogBuilder.setTitle(idOrder);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String berat = editTextBerat.getText().toString().trim();
                String harga = editTextHarga.getText().toString().trim();
                String parfurm = editTextParfurm.getText().toString().trim();
                String status = spinnerStatus.getSelectedItem().toString();
                if (!TextUtils.isEmpty(berat)) {
                    updateOrder(idOrder, idLaundry, idUser, namaUser, namaLaundry, alamatLaundry, alamatUser, tipe, berat, harga, status, parfurm);
                    b.dismiss();
                }
            }
        });
    }

    private boolean updateOrder(String idOrder, String idLaundry, String idUser, String namaUser, String namaLaundry,
                                String alamatLaundry, String alamatUser, String tipe, String berat,
                                String harga, String status, String parfurm) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("order").child(idOrder);

        //updating artist
        Order order = new Order(idOrder, idLaundry, idUser, namaUser, namaLaundry, alamatLaundry, alamatUser, tipe, berat, harga, status, parfurm);
        dR.setValue(order);
        Toast.makeText(getApplicationContext(), "Order Updated", Toast.LENGTH_LONG).show();
        return true;
    }
}
