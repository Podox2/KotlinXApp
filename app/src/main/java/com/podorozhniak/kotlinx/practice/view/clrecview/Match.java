package com.podorozhniak.kotlinx.practice.view.clrecview;

public class Match {
    private String result;
    private String season;

    public Match(String result, String season) {
        this.result = result;
        this.season = season;
    }

    public String getResult() {
        return result;
    }

    public String getSeason() {
        return season;
    }
}
