package com.rpl.kelompok1.gelolaundry.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.widget.ListView;

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
    Query query;
    FirebaseUser user;

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

        /*listViewOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                Order order = listOrder.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

                //putting artist name and id to intent
                intent.putExtra(ARTIST_ID, artist.getArtistId());
                intent.putExtra(ARTIST_NAME, artist.getArtistName());

                //starting the activity with intent
                startActivity(intent);
            }
        });*/


    }
}
