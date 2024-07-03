package com.example.myapp;

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

public class RegisterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button btnRegister = view.findViewById(R.id.btn_register);
        TextView txtLogin = view.findViewById(R.id.txt_login);
        EditText firstName = view.findViewById(R.id.edt_firstname);
        EditText lastName = view.findViewById(R.id.edt_lastname);
        EditText edt_email = view.findViewById(R.id.edt_username);
        EditText edt_pass = view.findViewById(R.id.edt_password);
        EditText edt_cfmPass = view.findViewById(R.id.edt_confirm_password);

        btnRegister.setOnClickListener(v -> {
            String email = edt_email.getText().toString();
            String password = edt_pass.getText().toString();
            String firstname = firstName.getText().toString();
            String lastname = lastName.getText().toString();
            String cfmpass = edt_cfmPass.getText().toString();

            if (!password.equals(cfmpass)){
                Toast.makeText(getContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            }else {
                register(email,password,firstname,lastname);
            }

        });
        txtLogin.setOnClickListener(v -> ((MainActivity) getActivity()).repalceFragment(new LoginFragment()));

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideBottomNavigationView();
    }

    private void register(String email, String password, String firstName, String lastName) {
        String url =  getString(R.string.baseURL) +"/auth/signup"; // Sửa lại URL ở đây

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("firstName",firstName);
            requestBody.put("lastName",lastName);
            requestBody.put("email", email);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    // Xử lý phản hồi nếu thành công
                    Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).repalceFragment(new LoginFragment());
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
