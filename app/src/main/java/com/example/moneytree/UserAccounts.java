package com.example.moneytree;


import androidx.collection.ArrayMap;

import com.example.moneytree.Model.NetWorkClass;

import java.util.ArrayList;

public class UserAccounts {

    private String uid;
    private String uname;
    private String uph;
    private int creditscore;
    private String imageurl;
    private int numOfNetworkscreated;
    private int numOfJoinedNetwork;



    public int getNumOfNetworkscreated() {
        return numOfNetworkscreated;
    }

    public void setNumOfNetworkscreated(int numOfNetworkscreated) {
        this.numOfNetworkscreated = numOfNetworkscreated;
    }

    public int getNumOfJoinedNetwork() {
        return numOfJoinedNetwork;
    }

    public void setNumOfJoinedNetwork(int numOfJoinedNetwork) {
        this.numOfJoinedNetwork = numOfJoinedNetwork;
    }

    public ArrayMap<String,NetWorkClass> netWorkClassArrayMap = new ArrayMap<>();
    public ArrayMap<String, NetWorkClass> availableNetworks= new ArrayMap<>();
    public ArrayList<String> searchResult=new ArrayList<>();





    private static UserAccounts obj;
    public static UserAccounts getUserObject(){
        if(obj==null){
            obj=new UserAccounts();
        }
        return obj;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUph() {
        return uph;
    }

    public void setUph(String uph) {
        this.uph = uph;
    }

    public int getCreditscore() {
        return creditscore;
    }

    public void setCreditscore(int creditscore) {
        this.creditscore = creditscore;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public static UserAccounts getObj() {
        return obj;
    }

    public static void setObj(UserAccounts obj) {
        UserAccounts.obj = obj;
    }


    public void addNetwork(String networkName,Double maxAmount,Integer noOfPeople){
        NetWorkClass netWorkClass = new NetWorkClass();
        netWorkClass.setTotalPeopleInMyNetwork(noOfPeople);
        netWorkClass.setMaxAmount(maxAmount);
        netWorkClassArrayMap.put(networkName,netWorkClass);

    }

    public void clearSearchList(){
        searchResult.clear();
    }
    public void clearAvailableNetworks(){
        availableNetworks.clear();
    }
    public void addToSearchList(String ele){
        searchResult.add(ele);
    }
    public void addToAvailableNetworks(String key,Double maxAmount,Integer noOfPeople){
        NetWorkClass netWorkClass = new NetWorkClass();
        netWorkClass.setTotalPeopleInMyNetwork(noOfPeople);
        netWorkClass.setMaxAmount(maxAmount);
        availableNetworks.put(key,netWorkClass);
    }
}
