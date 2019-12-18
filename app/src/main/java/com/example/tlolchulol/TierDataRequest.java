package com.example.tlolchulol;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


public class TierDataRequest extends StringRequest {

    final static private String URL = "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/";
    final static private String KEY = "?api_key=RGAPI-2466c0c0-1358-40e4-8e4e-380c06f1c7b7";

    public TierDataRequest(String summonerId, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.GET, URL +  summonerId + KEY, listener, errorListener);
    }
}
