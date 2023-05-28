package com.example.myproject2;

public class User {

    String fullName,overall,team, key;
    public User (String fullName,String overall,String team){
        this.team = team;
        this.fullName= fullName;
        this.overall = overall;
    }
    public User(String key, String fullName, String overall, String team) {
        this.key = key;
        this.fullName = fullName;
        this.overall = overall;
        this.team = team;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setOverall(String overall) {
        this.overall = overall;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTeam(){
        return team;
    }
    public String getFullName() {
        return fullName;
    }
    public String getOverall() {
        return overall;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
