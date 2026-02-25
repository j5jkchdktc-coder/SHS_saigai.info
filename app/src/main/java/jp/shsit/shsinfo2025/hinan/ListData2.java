package jp.shsit.shsinfo2025.hinan;

/**
 *  Created by shsit on 2021/04/21.
 */

public class ListData2 {
    String id;
    String name;
    String add;
    String kouzui;
    String jishin;
    String tsunami;
    String jyou;
    String lat;
    String lon;
    Double distance;
    Double bearing;


    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return id;
    }

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

    public void setKouzui(String kouzui){
        this.kouzui = kouzui;
    }
    public String getKouzui(){
        return kouzui;
    }

    public void setJishin(String jishin){
        this.jishin = jishin;
    }
    public String getJishin(){
        return jishin;
    }

    public void setTsunami(String tsunami){
        this.tsunami = tsunami;
    }
    public String getTsunami(){
        return tsunami;
    }

    public void setJyou(String jyou){
        this.jyou = jyou;
    }
    public String getJyou(){
        return jyou;
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


    public void setDistance(Double distance ){
        this.distance= distance;
    }
    public Double getDistance(){
        return distance;
    }
    public void setBearing(Double bearing ){
        this.bearing= bearing;
    }
    public Double getBearing(){
        return bearing;
    }

}