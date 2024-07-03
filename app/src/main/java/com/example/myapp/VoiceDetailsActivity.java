package com.example.myapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapp.model.Voice;

import java.io.IOException;

public class VoiceDetailsActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_details);

        ImageView thumbnails = findViewById(R.id.voice_details_thumbnails);
        TextView nameVoice = findViewById(R.id.voice_name);
        TextView description = findViewById(R.id.voice_description);
        Button playButton = findViewById(R.id.play_button);
        SeekBar seekBar = findViewById(R.id.seek_bar);

        Button buttonBack = findViewById(R.id.btn_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kết thúc Activity hiện tại và trở về Activity trước đó
            }
        });



        // Lấy dữ liệu từ Intent
        String id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String description_text = getIntent().getStringExtra("description");
        String thumbnailsUrl = getIntent().getStringExtra("thumbnails");
        String mp3Url = getIntent().getStringExtra("mp3Url");

        nameVoice.setText(name);
        description.setText(description_text);
        Glide.with(this).load(thumbnailsUrl).into(thumbnails);

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();
        playButton.setEnabled(false); // Disable play button until media is prepared

        // Set up MediaPlayer
        try {
            mediaPlayer.setDataSource(mp3Url); // Use the URL from the server
            mediaPlayer.setOnPreparedListener(mp -> {
                seekBar.setMax(mediaPlayer.getDuration());
                playButton.setEnabled(true); // Enable play button when media is prepared
                // Start updating seek bar
                updateSeekBar = new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null) {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            handler.postDelayed(this, 1000);
                        }
                    }
                };
                handler.post(updateSeekBar);
            });
            mediaPlayer.prepareAsync(); // Prepare asynchronously to not block the UI thread
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load audio", Toast.LENGTH_SHORT).show();
        }

        playButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playButton.setText("Play");
                playButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_play_arrow_24,0,0,0);
            } else {
                mediaPlayer.start();
                playButton.setText("Pause");
                playButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_pause_24,0,0,0);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updateSeekBar);
    }
}
