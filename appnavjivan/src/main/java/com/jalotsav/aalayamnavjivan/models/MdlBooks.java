package com.jalotsav.aalayamnavjivan.models;

/**
 * Created by Jalotsav on 1/17/2018.
 */

public class MdlBooks {

    String name, url, langShortCode;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public MdlBooks() {
    }

    public MdlBooks(String name, String url, String langShortCode) {
        this.name = name;
        this.url = url;
        this.langShortCode = langShortCode;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getLangShortCode() {
        return langShortCode;
    }
}
