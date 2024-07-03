package com.example.myapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button btnLogin;

    private static final String SHARED_PREFS = "shared_prefs";
    private static final String TOKEN_KEY = "token_key";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = view.findViewById(R.id.edt_username);
        passwordEditText = view.findViewById(R.id.edt_password);
        btnLogin = view.findViewById(R.id.btn_login);
        TextView txtRegister = view.findViewById(R.id.txt_register);
        TextView txtVisittor = view.findViewById(R.id.txt_visitor);
        btnLogin.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            login(email, password);
        });
        SharedPrefManager.getInstance(getContext()).clear();
        txtRegister.setOnClickListener(v -> ((MainActivity) getActivity()).repalceFragment(new RegisterFragment()));
        txtVisittor.setOnClickListener(v -> ((MainActivity) getActivity()).repalceFragment(new Home()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideBottomNavigationView();
    }

    private void login(String email, String password) {
        String url = getString(R.string.baseURL) +  "/auth/signin"; // Sửa lại URL ở đây

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", email);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    try {
                        String username = response.getString("username");
                        String firstname = response.getString("firstname");
                        String lastname = response.getString("lastname");
                        String token = response.getString("token");
                        String refreshToken = response.getString("refreshToken");
                        SharedPrefManager.getInstance(getContext()).saveUserInfo(username, firstname, lastname, token, refreshToken);                        // Xử lý phản hồi nếu thành công
                        ((MainActivity) getActivity()).repalceFragment(new Home());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    // Xử lý lỗi nếu có
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.statusCode == 403) {
                        // Thông báo lỗi cụ thể khi nhận được mã 403
                        Toast.makeText(getContext(), "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    } else {
                        String errorMessage = error.getMessage();
                        if (errorMessage == null) {
                            errorMessage = "Unknown error"; // Hoặc bất kỳ thông điệp mặc định nào bạn muốn
                        }
                        Log.e("LoginFragment", errorMessage);
                        Toast.makeText(getContext(), "Login failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Thêm yêu cầu vào RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }


}
