package com.example.nikita.customerapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nikita on 13-09-2016.
 */
public class MyTracking extends Activity {
    ListView listView;
    String customer_id;
    private ProgressDialog loading;
    ArrayList<HashMap<String, String>> vehicleList;
    int itemPosition;
    String itemValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking);
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listView);
        vehicleList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        customer_id = bundle.getString("Customer id");
        Toast.makeText(MyTracking.this,customer_id,Toast.LENGTH_LONG).show();
        getData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {



                // ListView Clicked item index
                //itemPosition = position;

                // ListView Clicked item value
               // itemValue = listView.getItemAtPosition(position);
                HashMap<String,String> map =(HashMap<String,String>)listView.getItemAtPosition(position);

                String vehicle= map.get(Config.KEY_VEHICLE);

                // Show Alert
               /* Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();*/
                Intent intent = new Intent(MyTracking.this,MainActivity.class);
                intent.putExtra("vehicle",vehicle);
                startActivity(intent);

            }

        });
        // Defined Array values to show in ListView
        /*String[] values = new String[] { "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });*/
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
                        Toast.makeText(MyTracking.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
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
            HashMap<String, String> myvehicle = new HashMap<>();
            myvehicle.put("vehicle", vehicle);
            vehicleList.add(myvehicle);
        }
        ListAdapter adapter = new SimpleAdapter(
                MyTracking.this, vehicleList,
                R.layout.list_vehicle, new String[]{"vehicle"}, new int[]{R.id.vehicle});
        listView.setAdapter(adapter);



    }
}


