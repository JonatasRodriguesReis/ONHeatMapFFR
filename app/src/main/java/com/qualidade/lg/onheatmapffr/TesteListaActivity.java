package com.qualidade.lg.onheatmapffr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import model.Data;
import model.ItemAdapter;
import model.ItemTeste;

public class TesteListaActivity extends AppCompatActivity {

    RecyclerView rv;
    LinearLayoutManager layoutManager;
    ItemAdapter adapter;
    ArrayList<ItemTeste> lista;
    Data dataApp;
    String produto = "";
    String estado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_lista);
        dataApp = (Data) getIntent().getSerializableExtra("dataApp");
        produto = getIntent().getStringExtra("produto");
        estado = getIntent().getStringExtra("estado");
        rv = findViewById(R.id.listaRecycle);
        lista = new ArrayList<ItemTeste>();
        adapter = new ItemAdapter(lista,this,dataApp,produto,estado);

        if(estado.equals("Brasil")){
            AsyncHttpClient client = new AsyncHttpClient();
            client.get("http://" + getResources().getString(R.string.ip) + ":85/api2/item/getItemsFFRBrasil.php?dia=" + dataApp.getDia()+ "&mes=" + dataApp.getMes()+ "&ano="+ dataApp.getNo()
                    + "&produto="+ produto ,null,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    ArrayList<ItemTeste> lists = new ArrayList<ItemTeste>();
                    try {
                        JSONArray items = response.getJSONArray("records");
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject object = items.getJSONObject(i);
                            Gson gson = new Gson();
                            ItemTeste item = gson.fromJson(object.toString(), ItemTeste.class);

                            lists.add(item);
                        }
                        for (ItemTeste i: lists) {
                            adapter.lista.add(i);
                        }
                        adapter.updateList(lists.size());

                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }else{
            AsyncHttpClient client = new AsyncHttpClient();

            client.get("http://" + getResources().getString(R.string.ip) + ":85/api2/item/getItemsFFR.php?dia=" + dataApp.getDia()+ "&mes=" + dataApp.getMes()+ "&ano="+ dataApp.getNo()
                    + "&produto="+ produto + "&estado=" + estado  ,null,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    ArrayList<ItemTeste> lists = new ArrayList<ItemTeste>();
                    try {
                        JSONArray items = response.getJSONArray("records");
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject object = items.getJSONObject(i);
                            Gson gson = new Gson();
                            ItemTeste item = gson.fromJson(object.toString(), ItemTeste.class);

                            lists.add(item);
                        }
                        for (ItemTeste i: lists) {
                            adapter.lista.add(i);
                        }
                        adapter.updateList(lists.size());

                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
        rv.setAdapter(adapter);

        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        LinearLayoutManager layout = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        rv.setLayoutManager(layout);
    }

}
