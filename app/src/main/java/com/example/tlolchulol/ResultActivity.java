package com.example.tlolchulol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ResultActivity extends AppCompatActivity {
    private TextView resultText;
    private RequestQueue queue;
    private JSONArray matchList;
    private String accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Realm realm = Realm.getDefaultInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultText = findViewById(R.id.resultText);
        Intent intent = getIntent();
        accountId = intent.getStringExtra("accountId");
        getMatchList();
//        0.5초후에 아래 동작을 실행
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                for (int i=0; i< matchList.length(); i++){
//                    JSONObject matchObject = matchList.getJSONObject(i);
//                    String gameId = matchObject.getString("gemeId");
//                    String lane = matchObject.getString("lane");
//                }
            }
        }, 500);
        System.out.println(matchList);


//        json파일을 db에 저장
//        try {
//            addChamp();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        final Champ champ = realm.where(Champ.class).equalTo("key", 5).findFirst();
//        System.out.println(champ.getImgUrl());

    }

    private void addChamp() throws JSONException {
        String json = "";
        Realm realm = Realm.getDefaultInstance();

        try {
            InputStream is = getAssets().open("champs.json");
            int fileSize = is.available();
            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray ChampArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < ChampArray.length(); i++) {
                JSONObject champObject = ChampArray.getJSONObject(i);
//                System.out.println(champObject.getString("name"));
                final Champ obj = new Champ();
                obj.setKey(Integer.parseInt(champObject.getString("key")));
                obj.setImgUrl(champObject.getString("name_en"));
                obj.setChampName(champObject.getString("name"));
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(obj);
                    }
                });
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void getMatchList() {
        Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    matchList = jsonObject.getJSONArray("matches");
                } catch (JSONException e) {
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };
        MatchListRequest matchListRequest = new MatchListRequest(accountId, responseListner, errorListener);
        queue = Volley.newRequestQueue(ResultActivity.this);
        queue.add(matchListRequest);
    }
}
