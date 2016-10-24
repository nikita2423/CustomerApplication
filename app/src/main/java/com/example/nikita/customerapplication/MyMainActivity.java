package com.example.nikita.customerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MyMainActivity extends AppCompatActivity {
    EditText customer;
    Button done;
    String customer_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer);
        customer = (EditText)findViewById(R.id.customer);
        done = (Button)findViewById(R.id.done);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customer_id = customer.getText().toString();
                Intent intent = new Intent(MyMainActivity.this,CardListActivity.class);
                intent.putExtra("Customer id",customer_id);
                startActivity(intent);
            }
        });
    }
}
