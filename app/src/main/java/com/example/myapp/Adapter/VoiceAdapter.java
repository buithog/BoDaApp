package com.example.myapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapp.ArticleDetailsActivity;
import com.example.myapp.R;
import com.example.myapp.VoiceDetailsActivity;
import com.example.myapp.model.Voice;

import java.util.List;

public class VoiceAdapter extends RecyclerView.Adapter<VoiceAdapter.VoiceViewHolder> {

    private List<Voice> voices;
    private OnItemClickListener listener;

    Context context;

    public void updateVoices(List<Voice> voices) {
        this.voices = voices;
        notifyDataSetChanged();
    }
    public VoiceAdapter(Context context, List<Voice> voices) {
        this.context = context;
        this.voices = voices;
    }
    public interface OnItemClickListener {
        void onItemClick(Voice voice);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public VoiceAdapter(List<Voice> voices) {
        this.voices = voices;
    }

    @NonNull
    @Override
    public VoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voice, parent, false);
        return new VoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoiceViewHolder holder, int position) {
        Voice voice = voices.get(position);
        holder.name.setText(voice.getName());
        Glide.with(holder.thumnails.getContext())
                .load(voice.getThumbnailUrl())
                .into(holder.thumnails);
        holder.itemView.setOnClickListener(
                v -> {
                    Intent intent = new Intent(context, VoiceDetailsActivity.class);
                    intent.putExtra("id",voice.getId());
                    intent.putExtra("name",voice.getName());
                    intent.putExtra("description",voice.getDescription());
                    intent.putExtra("thumbnails",voice.getThumbnailUrl());
                    intent.putExtra("mp3Url",voice.getMp3Url());

                    context.startActivity(intent);
                }
        );
    }

    @Override
    public int getItemCount() {
        return voices.size();
    }

    public static class VoiceViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumnails;
        private TextView name;


        public VoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            thumnails = itemView.findViewById(R.id.image_thumbnail);
            name = itemView.findViewById(R.id.text_title);
        }

    }
}
