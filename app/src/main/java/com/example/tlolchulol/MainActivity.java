package com.example.tlolchulol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Button btnSearch;
    private EditText searchSummoner;
    private String summoner;
    private RequestQueue queue;
    InputMethodManager imm;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.bntSearch);
        searchSummoner = findViewById(R.id.searchSummoner);
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summoner = searchSummoner.getText().toString();
                if (summoner.length() > 1 ){
                    Response.Listener<String> responseListner = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String accountId = jsonObject.getString("accountId");
                                String summonerId = jsonObject.getString("id");
                                intent.putExtra("accountId", accountId);
                                intent.putExtra("summonerId", summonerId);
                                searchSummoner.setText("");
                                startActivity(intent);
                            } catch (JSONException e) {
                            }
                        }
                    };

                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast = Toast.makeText(getApplicationContext(), "소환사 정보가 없습니다! 다시 검색해주세요.", Toast.LENGTH_SHORT);
                            toast.show();
                            searchSummoner.setText("");

                        }
                    };
                    SummonerRequest summonerRequest = new SummonerRequest(summoner, responseListner, errorListener);
                    queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(summonerRequest);
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "소환사 정보가 없습니다! 다시 검색해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    searchSummoner.setText("");
                }
            }
        });

    }
    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(searchSummoner.getWindowToken(), 0);
    }
}
