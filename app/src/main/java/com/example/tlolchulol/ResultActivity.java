package com.example.tlolchulol;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Build;
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
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ResultActivity extends AppCompatActivity {
//    private TextView resultText;
    private TextView winRate_40;
    private RequestQueue queue;
    private int[] laneCnt = new int[5];

    //  전체 rank 승률, 티어 변수
    private String soloRankTier, freeRankTier, soloRankWinLose, freeRankWinLose;
    //    40경기 평균 승률
    private int winCnt_40 = 0, killCnt_40 = 0, assistCnt_40 = 0, deathCnt_40 = 0;
    private double kdaRate_40 = 0.0;
    private int[] kdaCnt_40 = new int[4];
    private Realm realm = Realm.getDefaultInstance();
    private HashMap<Integer, Object> useChamp = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

//        resultText = findViewById(R.id.resultText);
        winRate_40 = findViewById(R.id.winRate);
        Intent intent = getIntent();
        String accountId = intent.getStringExtra("accountId");
        String summonerId = intent.getStringExtra("summonerId");
        getMatchList(accountId);
        getTierData(summonerId);


//        json파일을 db에 저장
//        try {
//            addChamp();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        db에서 불러올때


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

    private void getMatchList(final String accountId) {
        Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    final JSONArray matchList = jsonObject.getJSONArray("matches");
                    final Handler handler = new Handler();
                    for (int i = 0; i < 41; i++) {
                        final int finalI = i;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (finalI == 40) {
                                        kdaRate_40 = ((double) (killCnt_40 + assistCnt_40) / (double) (deathCnt_40));
                                        kdaRate_40 = Double.parseDouble(String.format("%.1f", kdaRate_40));
                                        String winRate = Double.toString(winCnt_40 / 40);
                                        winRate_40.setText(winRate);
                                        System.out.println(winCnt_40);
                                        System.out.println(kdaRate_40);
                                        System.out.println(kdaCnt_40[0]);
                                        System.out.println(kdaCnt_40[1]);
                                        System.out.println(kdaCnt_40[2]);
                                        System.out.println(kdaCnt_40[3]);
                                        System.out.println(useChamp.size());
                                        for (Integer key : useChamp.keySet()) {
                                            final Champ champ = realm.where(Champ.class).equalTo("key", key).findFirst();
                                            int[] tempArray = (int[]) useChamp.get(key);
                                            System.out.println(champ.getChampName());
                                            System.out.println(tempArray[0]);
                                            System.out.println(tempArray[1]);
                                            System.out.println(tempArray[2]);
                                            System.out.println(tempArray[3]);
                                            System.out.println(tempArray[4]);
                                        }
                                    } else {
                                        final JSONObject matchObject = matchList.getJSONObject(finalI);
                                        String gameId = matchObject.getString("gameId");
                                        String lane = matchObject.getString("lane");
                                        String role = matchObject.getString("role");
                                        cntLane(lane, role);
                                        getMatchDetail(gameId, accountId);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 400 * (i + 1));
                    }

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

    private void getMatchDetail(String gameId, final String accountId) {
        Response.Listener<String> responseListner = new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray players = jsonObject.getJSONArray("participantIdentities");
                    int participantId = 0;
                    for (int i = 0; i < players.length(); i++) {
                        JSONObject player = players.getJSONObject(i);
                        String player_accountId = player.getJSONObject("player").getString("accountId");
                        if (accountId.equals(player_accountId)) {
                            participantId = player.getInt("participantId");
                            break;
                        }
                    }
                    JSONArray statsArray = jsonObject.getJSONArray("participants");
                    for (int i = 0; i < statsArray.length(); i++) {
                        JSONObject statsObject = statsArray.getJSONObject(i);
                        if (participantId == statsObject.getInt("participantId")) {
                            JSONObject myStats = statsObject.getJSONObject("stats");
                            int champKey = statsObject.getInt("championId");
                            int tempArray[];
                            if (useChamp.containsKey(champKey)) {
                                tempArray = (int[]) useChamp.get(champKey);
                            } else {
                                tempArray = new int[5];
                            }
                            tempArray[0] += 1;
                            if (myStats.getBoolean("win")) {
                                winCnt_40++;
                                tempArray[1] += 1;
                            }
                            int kill = myStats.getInt("kills");
                            killCnt_40 += kill;
                            tempArray[2] += kill;

                            int assist = myStats.getInt("assists");
                            assistCnt_40 += assist;
                            tempArray[3] += assist;

                            int death = myStats.getInt("deaths");
                            deathCnt_40 += death;
                            tempArray[4] += death;

                            if (useChamp.containsKey(champKey)) {
                                useChamp.replace(champKey, tempArray);
                            } else {
                                useChamp.put(champKey, tempArray);
                            }

                            death = (death == 0) ? 1 : death;
                            double kda = ((double) (kill + assist) / (double) (death));
                            kda = Double.parseDouble(String.format("%.1f", kda));
                            cntKda(kda);
                            break;
                        }
                    }

                } catch (JSONException e) {
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };
        MatchDetailRequest matchDetailRequest = new MatchDetailRequest(gameId, responseListner, errorListener);
        queue = Volley.newRequestQueue(ResultActivity.this);
        queue.add(matchDetailRequest);
    }

    private void cntLane(String lane, String role) {
        if (lane == "TOP") {
            laneCnt[0]++;
        } else if (lane == "JUNGLE") {
            laneCnt[1]++;
        } else if (lane == "JUNGLE") {
            laneCnt[2]++;
        } else if (lane == "BOTTOM") {
            if (role == "DUO_CARRY") {
                laneCnt[3]++;
            } else {
                laneCnt[4]++;
            }
        }
    }

    private void getTierData(String summonerId) {
        Response.Listener<String> responseListner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray tierDatas = new JSONArray(response);
                    for (int i = 0; i < tierDatas.length(); i++) {
                        JSONObject tierObject = tierDatas.getJSONObject(i);
                        if (tierObject.getString("queueType").equals("RANKED_SOLO_5x5")) {
                            soloRankTier = tierObject.getString("tier") + " " + tierObject.getString("rank");
                            int win = tierObject.getInt("wins");
                            int lose = tierObject.getInt("losses");
                            double rate = ((double) win / (double) (win + lose)) * 100;
                            rate = Double.parseDouble(String.format("%.2f", rate));
                            soloRankWinLose = "WIN:" + win + " LOSE:" + lose + " (" + rate + "%)";

                        } else if (tierObject.getString("queueType").equals("RANKED_FLEX_SR")) {
                            freeRankTier = tierObject.getString("tier") + " " + tierObject.getString("rank");
                            int win = tierObject.getInt("wins");
                            int lose = tierObject.getInt("losses");
                            double rate = ((double) win / (double) (win + lose)) * 100;
                            rate = Double.parseDouble(String.format("%.2f", rate));
                            freeRankWinLose = "WIN:" + win + " LOSE:" + lose + " (" + rate + "%)";
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };
        TierDataRequest tierDataRequest = new TierDataRequest(summonerId, responseListner, errorListener);
        queue = Volley.newRequestQueue(ResultActivity.this);
        queue.add(tierDataRequest);
    }

    private void cntKda(double kda) {
        if (kda <= 0.5) {
            kdaCnt_40[0]++;
        } else if (kda <= 1.0) {
            kdaCnt_40[1]++;
        } else if (kda <= 2.0) {
            kdaCnt_40[2]++;
        } else {
            kdaCnt_40[3]++;
        }
    }
}
