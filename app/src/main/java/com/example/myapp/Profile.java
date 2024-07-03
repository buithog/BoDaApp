package com.example.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Profile extends Fragment {

    private TextView tvFirstName, tvLastName, tvEmail;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        tvFirstName = view.findViewById(R.id.tv_first_name);
        tvLastName = view.findViewById(R.id.tv_last_name);
        tvEmail = view.findViewById(R.id.tv_email);
        btnLogout = view.findViewById(R.id.btn_logout);


        String username = SharedPrefManager.getInstance(getContext()).getUsername();
        String firstname = SharedPrefManager.getInstance(getContext()).getFirstname();
        String lastname = SharedPrefManager.getInstance(getContext()).getLastname();
        // Set user information (replace these with actual data)
        tvFirstName.setText(firstname);
        tvLastName.setText(lastname);
        tvEmail.setText(username);

        // Set logout button click listener
        btnLogout.setOnClickListener(v -> {
            // Navigate to the LoginFragment
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new LoginFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).showBottomNavigationView();
    }
}
