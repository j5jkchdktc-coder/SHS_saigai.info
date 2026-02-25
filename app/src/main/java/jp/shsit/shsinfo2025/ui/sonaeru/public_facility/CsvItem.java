package jp.shsit.shsinfo2025.ui.sonaeru.public_facility;

class CsvItem {
    String kind;
    String name;
    String lat;
    String lng;
    String draw;

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getDraw() {
        return draw;
    }

}
