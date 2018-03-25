package com.qualidade.lg.onheatmapffr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import model.Cidade;
import model.Data;

public class ListaCidadesActivity extends AppCompatActivity {
    private ListView listview;
    private ArrayAdapter listAdapter;
    private String nomeItem;
    private String estado;
    private String produto;
    private Data dataApp;
    private ArrayList<Cidade> cidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cidades);
        nomeItem = getIntent().getStringExtra("nome");
        estado = getIntent().getStringExtra("estado");
        produto = getIntent().getStringExtra("produto");
        dataApp = (Data) getIntent().getSerializableExtra("dataApp");
        final ArrayList<String> arrayCidades = new ArrayList<String>();

        this.listview = (ListView) findViewById(R.id.lsvCities);

        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayCidades);
        listview.setAdapter(listAdapter);

        if(estado.equals("Brasil")){
            AsyncHttpClient client = new AsyncHttpClient();

            client.get("http://" + getResources().getString(R.string.ip) + ":85/api2/cidade/getCidadesBrasil.php?dia=" + dataApp.getDia()+ "&mes=" + dataApp.getMes()+ "&ano="+ dataApp.getNo()
                    + "&produto="+ produto + "&item=" + nomeItem,null,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    ArrayList<Cidade> lists = new ArrayList<Cidade>();
                    try {
                        JSONArray cidades = response.getJSONArray("records");
                        for (int i = 0; i < cidades.length(); i++) {
                            JSONObject object = cidades.getJSONObject(i);
                            Gson gson = new Gson();
                            Cidade cidade = gson.fromJson(object.toString(), Cidade.class);

                            lists.add(cidade);
                        }
                        for (Cidade i: lists) {
                            arrayCidades.add(i.getNome());
                        }


                        listview.setAdapter(listAdapter);


                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }else{
            AsyncHttpClient client = new AsyncHttpClient();

            client.get("http://" + getResources().getString(R.string.ip) + ":85/api2/cidade/getCidades.php?dia=" + dataApp.getDia()+ "&mes=" + dataApp.getMes()+ "&ano="+ dataApp.getNo()
                    + "&produto="+ produto + "&estado=" + estado + "&item=" + nomeItem,null,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    ArrayList<Cidade> lists = new ArrayList<Cidade>();
                    try {
                        JSONArray cidades = response.getJSONArray("records");
                        for (int i = 0; i < cidades.length(); i++) {
                            JSONObject object = cidades.getJSONObject(i);
                            Gson gson = new Gson();
                            Cidade cidade = gson.fromJson(object.toString(), Cidade.class);

                            lists.add(cidade);
                        }
                        for (Cidade i: lists) {
                            arrayCidades.add(i.getNome());
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
                Intent intent = new Intent(getApplicationContext(),ListaAutorizadasActivity.class);
                intent.putExtra("dataApp", dataApp);
                intent.putExtra("produto", produto);
                intent.putExtra("estado", estado);
                intent.putExtra("nome", nomeItem);
                intent.putExtra("cidade", arrayCidades.get(position));
                startActivity(intent);
            }
        });
    }

}
