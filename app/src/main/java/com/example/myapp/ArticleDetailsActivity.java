package com.example.myapp;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ArticleDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);

        ImageView imageView = findViewById(R.id.article_image);
        TextView titleTextView = findViewById(R.id.article_title);
        TextView authorTextView = findViewById(R.id.article_author);
        TextView articleContentTextView = findViewById(R.id.article_content);
        Button btnBack;
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trở về trang trước đó
                onBackPressed();
            }
        });
        // Nhận dữ liệu từ Intent
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String content = getIntent().getStringExtra("content");


        titleTextView.setText(title);
        authorTextView.setText(author);
        articleContentTextView.setText(content);
        Glide.with(this).load(imageUrl).into(imageView);
//        imageView.setImageResource(R.drawable.chuaboda16863);
    }
}