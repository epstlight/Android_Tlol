package com.example.tlolchulol;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


public class MatchListRequest extends StringRequest {

    final static private String URL = "https://kr.api.riotgames.com/lol/match/v4/matchlists/by-account/";
    final static private String KEY = "&api_key=RGAPI-3488b9f2-e9c8-4043-aa8d-5e04b2c274fa";


    public MatchListRequest(String accountId, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.GET, URL +  accountId + "?queue=420&queue=440"+ KEY, listener, errorListener);
    }
}
