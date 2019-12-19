package com.example.tlolchulol;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.StringRequest;



public class SummonerRequest extends StringRequest {

    final static private String URL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/";
    final static private String KEY = "/?api_key=RGAPI-3488b9f2-e9c8-4043-aa8d-5e04b2c274fa";


    public SummonerRequest(String summonerName, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.GET, URL + summonerName + KEY, listener, errorListener);
    }

}
