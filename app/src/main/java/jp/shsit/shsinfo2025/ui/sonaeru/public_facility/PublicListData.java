package jp.shsit.shsinfo2025.ui.sonaeru.public_facility;

/**
 *  Created by shsit on 2021/04/21.
 */

public class PublicListData {

    String name;
    String add;
    String lat;
    String lon;



    public void setName (String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setAddress(String add){
        this.add = add;
    }
    public String getAddress(){
        return add;
    }


    public void setLat(String lat){
        this.lat = lat;
    }
    public String getLat(){
        return lat;
    }

    public void setLon(String lon){
        this.lon = lon;
    }
    public String getLon(){
        return lon;
    }
}