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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>  {

    private List<Article> articleList;
    private Context context;

    public PostAdapter(Context context,List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
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
    public static class PostViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnailImageView;
        public TextView titleTextView;
        public TextView authorTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.post_thumbnail);
            titleTextView = itemView.findViewById(R.id.post_title);
            authorTextView = itemView.findViewById(R.id.post_author);
        }
    }


}
