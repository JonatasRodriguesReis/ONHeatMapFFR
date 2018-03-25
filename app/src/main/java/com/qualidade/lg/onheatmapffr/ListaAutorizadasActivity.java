package com.qualidade.lg.onheatmapffr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import model.Autorizada;
import model.Data;

public class ListaAutorizadasActivity extends AppCompatActivity {
    private ListView listview;
    private ArrayAdapter listAdapter;
    private Data dataApp;
    private String produto;
    private String estado;
    private String nomeItem;
    private String cidadeNome;
    private ArrayList<Autorizada> autorizadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_autorizadas);
        nomeItem = getIntent().getStringExtra("nome");
        estado = getIntent().getStringExtra("estado");
        produto = getIntent().getStringExtra("produto");
        dataApp = (Data) getIntent().getSerializableExtra("dataApp");
        cidadeNome = getIntent().getStringExtra("cidade");
        Log.d("Teste2",cidadeNome);
        final ArrayList<String> arrayAutorizadas = new ArrayList<String>();

        this.listview = (ListView) findViewById(R.id.lsvAutorizadas);

        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayAutorizadas);
        listview.setAdapter(listAdapter);

        if(estado.equals("Brasil")){
            AsyncHttpClient client = new AsyncHttpClient();
            client.get("http://" + getResources().getString(R.string.ip) + ":85/api2/autorizada/getAutorizadasBrasil.php?dia=" + dataApp.getDia()+ "&mes=" + dataApp.getMes()+ "&ano="+ dataApp.getNo()
                    + "&produto="+ produto + "&item=" + nomeItem + "&cidade=" + cidadeNome,null,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    ArrayList<Autorizada> lists = new ArrayList<Autorizada>();
                    try {
                        JSONArray autorizadas = response.getJSONArray("records");
                        for (int i = 0; i < autorizadas.length(); i++) {
                            JSONObject object = autorizadas.getJSONObject(i);
                            Gson gson = new Gson();
                            Autorizada autorizada = gson.fromJson(object.toString(), Autorizada.class);

                            lists.add(autorizada);
                        }
                        for (Autorizada i: lists) {
                            arrayAutorizadas.add(i.getNome());
                        }


                        listview.setAdapter(listAdapter);


                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }else{
            AsyncHttpClient client = new AsyncHttpClient();
            client.get("http://" + getResources().getString(R.string.ip) + ":85/api2/autorizada/getAutorizadas.php?dia=" + dataApp.getDia()+ "&mes=" + dataApp.getMes()+ "&ano="+ dataApp.getNo()
                    + "&produto="+ produto + "&estado=" + estado + "&item=" + nomeItem + "&cidade=" + cidadeNome,null,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    ArrayList<Autorizada> lists = new ArrayList<Autorizada>();
                    try {
                        JSONArray autorizadas = response.getJSONArray("records");
                        for (int i = 0; i < autorizadas.length(); i++) {
                            JSONObject object = autorizadas.getJSONObject(i);
                            Gson gson = new Gson();
                            Autorizada autorizada = gson.fromJson(object.toString(), Autorizada.class);

                            lists.add(autorizada);
                        }
                        for (Autorizada i: lists) {
                            arrayAutorizadas.add(i.getNome());
                        }


                        listview.setAdapter(listAdapter);


                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });
    }

}
