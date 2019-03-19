package com.example.yash.music.Model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Song implements Serializable {

    private String songName,songArtist,songUrl;
    private Bitmap songimage;

    public Song(String songName, String songArtist, String songUrl, Bitmap songimage) {
        this.songName = songName;
        this.songArtist = songArtist;
        this.songUrl = songUrl;
        this.songimage=songimage;
    }

    public Song() {
    }

    public String getSongName() {
        return songName;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public Bitmap getSongimage(){return songimage;}
}
