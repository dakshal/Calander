package com.example.vidhi.calendar;


public class Response {
    private String title;
    private String letter;

    public Response() {
    }

    public Response(String title, String letter) {
        this.title = title;
        this.letter = letter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
