package com.example.myapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapp.Adapter.PostAdapter;
import com.example.myapp.model.Article;
import com.example.myapp.model.Post;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class postPage extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Article> postList;

    private RequestQueue requestQueue;
    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        requestQueue = Volley.newRequestQueue(getContext());
        gson = new Gson();

        recyclerView = view.findViewById(R.id.recycler_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(),postList);
        recyclerView.setAdapter(postAdapter);

        recyclerView.addItemDecoration(new SpacesItemDecoration(10));

        fetchData();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).showBottomNavigationView();
    }


    private void fetchData() {
        String url = getString(R.string.baseURL) + "/post?pageSize=10"; // Thay bằng URL của API của bạn

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("API Response", response.toString());
//                    Toast.makeText(getContext(), "Data fetched successfully!", Toast.LENGTH_SHORT).show();

                    // Xử lý phản hồi JSON
                    Type listType = new TypeToken<List<Post>>(){}.getType();
                    List<Post> posts = gson.fromJson(response.toString(), listType);

                    List<Article> articles = new ArrayList<>();
                    for (Post post : posts) {
                        String author = post.getAuthor();
                        String title = post.getTitle();
                        String imgThumbnails = getString(R.string.baseURL) + "/image/imagethumbnails?id=" + post.getId();
                        StringBuilder content = new StringBuilder();
                        for (String s: post.getContent()){
                            content.append(s).append("\n");
                        }
                        articles.add(new Article(title, author, imgThumbnails, content.toString()));
                    }

                    // Cập nhật adapter với dữ liệu mới
                    postAdapter.updateArticles(articles);
                },
                error -> {
                    // Xử lý lỗi nếu có
//                    Toast.makeText(getContext(), "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonArrayRequest);
    }

}