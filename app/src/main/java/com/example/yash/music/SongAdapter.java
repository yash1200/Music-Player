package com.example.yash.music;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yash.music.Model.Song;

import java.io.Serializable;
import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    Context context;
    ArrayList<Song> songs;
    private OnItemClicked onClick;

    public SongAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
    }


    @NonNull
    @Override
    public SongAdapter.SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.custom_list,parent,false);
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.SongHolder holder, final int position) {
        SongHolder.songname.setText(songs.get(position).getSongName()
                .replace(".mp3","")
                .replace(".amr","")
                .replace(".opus","")
                .replace(".wav",""));
        SongHolder.artistname.setText(songs.get(position).getSongArtist());
        if(songs.get(position).getSongimage()!=null)
        {
            SongHolder.image.setImageBitmap(songs.get(position).getSongimage());
        }
        else
        {
            SongHolder.image.setImageResource(R.drawable.ic_music);
        }

        SongHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });

    }

    public interface OnItemClicked
    {
        void onItemClick(int position);
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongHolder extends RecyclerView.ViewHolder {

        static TextView songname;
        static ImageView image;
        static TextView artistname;
        static RelativeLayout relativeLayout;
        public SongHolder(View itemView) {
            super(itemView);
            songname=itemView.findViewById(R.id.song_name);
            artistname=itemView.findViewById(R.id.artist_name);
            image=itemView.findViewById(R.id.image);
            relativeLayout=itemView.findViewById(R.id.relative);
        }
    }
}