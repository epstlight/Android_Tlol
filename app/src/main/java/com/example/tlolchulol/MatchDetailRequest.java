package com.example.tlolchulol;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


public class MatchDetailRequest extends StringRequest {

    final static private String URL = "https://kr.api.riotgames.com/lol/match/v4/matches/";
    final static private String KEY = "?api_key=RGAPI-2466c0c0-1358-40e4-8e4e-380c06f1c7b7";


    public MatchDetailRequest(String gameId, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.GET, URL +  gameId + KEY, listener, errorListener);
    }
}
