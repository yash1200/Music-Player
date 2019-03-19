package com.example.yash.music;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.yash.music.Model.Song;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayInputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SongAdapter.OnItemClicked {

    RecyclerView recyclerView;
    SongAdapter songAdapter;
    ArrayList<Song> songs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songs = new ArrayList<>();
        recyclerView= findViewById(R.id.recyclerView);
        songAdapter = new SongAdapter(MainActivity.this, songs);
        recyclerView.setAdapter(songAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        songAdapter.setOnClick(MainActivity.this);
        checkUserPermission();
    }

    public void onItemClick(int position)
    {
        startActivity(new Intent(MainActivity.this,MusicActivity.class).putExtra("songs",songs));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
                break;
            case R.id.about:
                startActivity(new Intent(MainActivity.this,AboutActivity.class));

        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 123 && resultCode==RESULT_OK){
            loadSongs();
        }
    }

    private void checkUserPermission(){
        if(Build.VERSION.SDK_INT>=24){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
                return;
            } else {
                loadSongs();
            }
        }
    }

    private void loadSongs(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC+"!=0";
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    Uri songUri=
                            ContentUris
                                    .withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                            cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    System.out.println(name);
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(MainActivity.this, songUri);
                    byte[] artBytes =  mmr.getEmbeddedPicture();
                    Bitmap bitmap = null;
                    if(artBytes != null) {
                        InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
                        bitmap = BitmapFactory.decodeStream(is);
                    }
                    Song s = new Song(name,artist,url,bitmap);
                    songs.add(s);
                    songAdapter.notifyDataSetChanged();
                }while (cursor.moveToNext());
            }

            cursor.close();

        }
    }


}