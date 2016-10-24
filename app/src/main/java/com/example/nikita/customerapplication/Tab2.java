package com.example.nikita.customerapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Nikita on 25-09-2016.
 */
public class Tab2 extends Fragment {

    ListView listView;
    private CardArrayAdapter2 cardArrayAdapter;
    String customer_id, vehicle;
    private ProgressDialog loading;
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.details_listview, container, false);
        listView = (ListView)view.findViewById(R.id.card_listView);
        Bundle bundle = getActivity().getIntent().getExtras();
        customer_id = bundle.getString("Customer id");
        vehicle = bundle.getString("vehicle");
        //Toast.makeText(getActivity(),"Tab2" + customer_id,Toast.LENGTH_LONG).show();
        //Toast.makeText(getActivity(),"Tab2" + vehicle,Toast.LENGTH_LONG).show();
        listView.addHeaderView(new View(getActivity().getApplicationContext()));
        listView.addFooterView(new View(getActivity().getApplicationContext()));
        cardArrayAdapter = new CardArrayAdapter2(getActivity().getApplicationContext(), R.layout.details_list_item_card);
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        getData();
        return view;
    }
    private void getData() {
        String customer = customer_id;
        String myvehicle = vehicle;
        //Toast.makeText(getActivity(),customer + " " + myvehicle,Toast.LENGTH_LONG).show();
        loading = ProgressDialog.show(getActivity(), "Please wait...", "Fetching...", false, false);

       // String url = Config.FETCH_DETAILS_URL+myvehicle && customer;
          String url = "http://myvendor.pe.hu/AndroidOTP/fetch_details.php?vehicle=" + myvehicle + "&customer_id="+ customer;
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
                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private void showJSON(String response) throws JSONException {
       // Toast.makeText(getActivity(),"showresponse" + response,Toast.LENGTH_LONG).show();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray details = jsonObject
                .getJSONArray(Config.JSON_ARRAY); // JSON Array
        Toast.makeText(getActivity(),"length" + String.valueOf(details.length()),Toast.LENGTH_LONG).show();
        for(int i=0;i<details.length();i++){
            JSONObject v = details.getJSONObject(i);
            String date = v.getString("my_date");
            String distance = v.getString("distance");
            String time = v.getString("time");
            //Toast.makeText(getActivity(),"show json" + date + " " + distance + " " + time,Toast.LENGTH_LONG).show();
            Card2 card = new Card2(date,distance,time);
            cardArrayAdapter.add(card);
        }
        listView.setAdapter(cardArrayAdapter);




    }
}
