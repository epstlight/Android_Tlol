package com.example.tlolchulol;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


public class MatchListRequest extends StringRequest {

    final static private String URL = "https://kr.api.riotgames.com/lol/match/v4/matchlists/by-account/";
    final static private String KEY = "&api_key=RGAPI-4a21855e-7367-4dce-b5a7-4ff22ca1704a";


    public MatchListRequest(String accountId, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.GET, URL +  accountId + "?queue=420&queue=440"+ KEY, listener, errorListener);
    }
}
