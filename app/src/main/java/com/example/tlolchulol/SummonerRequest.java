package com.example.tlolchulol;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.StringRequest;



public class SummonerRequest extends StringRequest {

    final static private String URL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/";
    final static private String KEY = "/?api_key=RGAPI-2466c0c0-1358-40e4-8e4e-380c06f1c7b7";


    public SummonerRequest(String summonerName, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.GET, URL + summonerName + KEY, listener, errorListener);
    }

}
