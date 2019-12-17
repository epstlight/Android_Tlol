package com.example.tlolchulol;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.StringRequest;



public class SummonerRequest extends StringRequest {

    final static private String URL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/";
    final static private String KEY = "/?api_key=RGAPI-4a21855e-7367-4dce-b5a7-4ff22ca1704a";


    public SummonerRequest(String summonerName, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.GET, URL + summonerName + KEY, listener, errorListener);
    }

}
