package com.example.myapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapp.ArticleDetailsActivity;
import com.example.myapp.R;
import com.example.myapp.model.Article;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articleList;
    private Context context;

    public ArticleAdapter(List<Article> articleList) {
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.authorTextView.setText(article.getAuthor());

//         Set thumbnail image using Glide
        Glide.with(holder.thumbnailImageView.getContext())
                .load(article.getThumbnailUrl())
                .into(holder.thumbnailImageView);
//        holder.thumbnailImageView.setImageResource(R.drawable.chuaboda16863);
        holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ArticleDetailsActivity.class);
                intent.putExtra("imageUrl", article.getThumbnailUrl());
                intent.putExtra("title", article.getTitle());
            intent.putExtra("author", article.getAuthor());
            intent.putExtra("content",article.getContent());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public void updateArticles(List<Article> newArticles) {
        this.articleList = newArticles;
        notifyDataSetChanged();
    }
    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnailImageView;
        public TextView titleTextView;
        public TextView authorTextView;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.image_thumbnail);
            titleTextView = itemView.findViewById(R.id.text_title);
            authorTextView = itemView.findViewById(R.id.text_author);
        }
    }
}