package org.example;

public class NASA {

    String copyright;
    String date;
    String explanation;
    String hdurl;
    String media_type;
    String service_version;
    String title;
    String url;

    public NASA() {
        // Пустой конструктор
    }

    public NASA(String copyright, String date, String explanation, String hdurl, String media_type, String service_version, String title, String url) {
        this.copyright = copyright;
        this.date = date;
        this.explanation = explanation;
        this.hdurl = hdurl;
        this.media_type = media_type;
        this.service_version = service_version;
        this.title = title;
        this.url = url;
    }

    public String getURL() {
        return url;
    }

}
