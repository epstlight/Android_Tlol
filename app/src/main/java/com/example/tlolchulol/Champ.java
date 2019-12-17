package com.example.tlolchulol;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Champ extends RealmObject {
    private int key;
    private String champName;
    private String imgUrl;


    public Champ() {
    }

    public Champ(int key, String champName) {
        this.key = key;
        this.champName = champName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = "http://ddragon.leagueoflegends.com/cdn/9.24.2/img/champion/" + imgUrl + ".png";
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getChampName() {
        return champName;
    }

    public void setChampName(String champName) {
        this.champName = champName;
    }
}
