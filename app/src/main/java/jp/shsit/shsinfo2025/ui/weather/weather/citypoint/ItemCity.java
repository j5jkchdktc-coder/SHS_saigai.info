package jp.shsit.shsinfo2025.ui.weather.weather.citypoint;

/**
 *  Created by shsit on 2025/04/30.
 */

public class ItemCity {
    // 都市
    private CharSequence City;
    private CharSequence City_eng;
    private CharSequence City_ch;
    private CharSequence City_vn;
    // no
    private CharSequence No;

    private CharSequence Area;


    public ItemCity() {
        City = "";
        City_eng = "";
        City_vn = "";
        City_ch = "";
        No = "";
        Area = "";
    }


    public CharSequence getCity() {
        return City;
    }
    public CharSequence getCity_eng() {
        return City_eng;
    }
    public CharSequence getCity_ch() { return City_ch; }
    public CharSequence getCity_vn() { return City_vn; }
    public void setCity(CharSequence city) {
        City = city;
    }
    public void setCity_eng(CharSequence city_eng) {
        City_eng = city_eng;
    }
    public void setCity_ch(CharSequence city_ch) { City_ch = city_ch; }
    public void setCity_vn(CharSequence city_vn) { City_vn = city_vn; }
    public CharSequence getNo() {
        return No;
    }
    public void setNo(CharSequence no) {
        No= no;
    }

    public CharSequence getArea() { return Area; }
    public void setArea(CharSequence area) { Area = area; }


}
