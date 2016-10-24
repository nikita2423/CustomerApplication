package com.example.nikita.customerapplication;

/**
 * Created by Nikita on 25-09-2016.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CardListActivity extends AppCompatActivity {

    private static final String TAG = "CardListActivity";
    private CardArrayAdapter cardArrayAdapter;
    private ListView listView;
    String customer_id;
    private ProgressDialog loading;
    int itemPosition;
    String vehicle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        listView = (ListView) findViewById(R.id.card_listView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        customer_id = bundle.getString("Customer id");
        //Toast.makeText(CardListActivity.this, customer_id, Toast.LENGTH_LONG).show();

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));

        cardArrayAdapter = new CardArrayAdapter(getApplicationContext(), R.layout.list_item_card);
        getData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                // ListView Clicked item index
                //itemPosition = position;
               // String vehicle =
                //FrameLayout linearLayoutParent = (FrameLayout) view;

                // Getting the inner Linear Layout
               // LinearLayout linearLayoutChild = (LinearLayout) linearLayoutParent.getChildAt(1);

                // Getting the Country TextView
               // TextView vehicle = (TextView) linearLayoutChild.getChildAt(0);
                //String vehicle = ((FrameLayout)view).getText().toString();
                // ListView Clicked item value
                 //vehicle = listView.getItemAtPosition(position);
                //HashMap<String,String> map = (HashMap<String, String>) listView.getItemAtPosition(position);

                //String vehicle = map.get(Config.KEY_VEHICLE);

                // Show Alert
               /* Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();*/
               /* Intent intent = new Intent(CardListActivity.this, MainActivity.class);
                intent.putExtra("vehicle", vehicle.getText().toString());
                startActivity(intent);*/
                Card cards= (Card) parent.getItemAtPosition(position);
             // Toast.makeText(CardListActivity.this,cards.getLine1(),Toast.LENGTH_LONG).show();
                 Intent intent = new Intent(CardListActivity.this, MainActivity.class);
                intent.putExtra("Customer id",customer_id);
                intent.putExtra("vehicle", cards.getLine1());
                startActivity(intent);
                //finish();
            }

        });

    }
    private void getData() {
        String customer = customer_id;

        loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);

        String url = Config.DATA_URL+customer;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try {
                    showJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CardListActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void showJSON(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray vehicles = jsonObject
                .getJSONArray(Config.JSON_ARRAY); // JSON Array
        for(int i=0;i<vehicles.length();i++){
            JSONObject v = vehicles.getJSONObject(i);
            String vehicle = v.getString("vehicle");
            Card card = new Card(vehicle);
            cardArrayAdapter.add(card);
                   }
        listView.setAdapter(cardArrayAdapter);




    }
}
