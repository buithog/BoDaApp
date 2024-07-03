package com.example.myapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapp.Adapter.ArticleAdapter;
import com.example.myapp.Adapter.VoiceAdapter;
import com.example.myapp.model.Article;
import com.example.myapp.model.Post;
import com.example.myapp.model.Voice;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Home extends Fragment {

    private RecyclerView recyclerView, recyclerView2;
    private ArticleAdapter adapter;
    private VoiceAdapter voiceAdapter;
    private RequestQueue requestQueue;
    private Gson gson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        requestQueue = Volley.newRequestQueue(getContext());
        gson = new Gson();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView2 = view.findViewById(R.id.second_recycler_view);
        TextView datetime = view.findViewById(R.id.datetime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd/MM/yyyy", Locale.getDefault());
        String date = dateFormat.format(new Date());

        datetime.setText(date);

        voiceAdapter = new VoiceAdapter(getContext(),new ArrayList<>());
        recyclerView2.setAdapter(voiceAdapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        recyclerView2.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        adapter = new ArticleAdapter(new ArrayList<>()); // Khởi tạo với danh sách rỗng
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        // get data user.
        TextView nameuser = view.findViewById(R.id.tv_fullname);
        TextView username = view.findViewById(R.id.tv_email);

        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getContext());
        String firstname = sharedPrefManager.getFirstname();
        String lastname = sharedPrefManager.getLastname();
        String email = sharedPrefManager.getUsername();
        if (firstname!= null && lastname != null && email != null){
            nameuser.setText(firstname +" "+lastname);
            username.setText(email);
        }else {
            nameuser.setText("Visiter");
            username.setText(" ");
        }
        fetchData();
        fetchDataVoice();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).showBottomNavigationView();
    }

    private void fetchData() {
        String url = getString(R.string.baseURL) + "/post"; // Thay bằng URL của API của bạn

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("API Response", response.toString());
                    Toast.makeText(getContext(), "Data fetched successfully!", Toast.LENGTH_SHORT).show();

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
                    adapter.updateArticles(articles);
                },
                error -> {
                    // Xử lý lỗi nếu có
                    Toast.makeText(getContext(), "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonArrayRequest);
    }
    private void fetchDataVoice(){
        String url = getString(R.string.baseURL) + "/voice/getvoice"; // Thay bằng URL của API của bạn

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("API Response", response.toString());
                    Toast.makeText(getContext(), "Data fetched successfully!", Toast.LENGTH_SHORT).show();

                    // Xử lý phản hồi JSON
                    List<Voice> voices = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String name = jsonObject.getString("name");
                            String description = jsonObject.getString("description");
                            String thumbnailsUrl = getString(R.string.baseURL)+ "/voice/voicethumnails/"+id;
                            String fileMp3Url = getString(R.string.baseURL) + "/voice/voicefile/" + id;
                            voices.add(new Voice(id, name,thumbnailsUrl,fileMp3Url,description,null));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    // Cập nhật adapter với dữ liệu mới
                    voiceAdapter.updateVoices(voices);
                },
                error -> {
                    // Xử lý lỗi nếu có
                    Toast.makeText(getContext(), "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonArrayRequest);
    }
}
