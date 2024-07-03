package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.myapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repalceFragment(new LoginFragment());

        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
                    switch (item.getItemId()){
                        case R.id.home:
                            repalceFragment(new Home());
                            break;
                        case R.id.scan:
                            repalceFragment(new Scan());
                            break;
                        case R.id.post:
                            repalceFragment(new postPage());
                            break;
                        case R.id.profile:
                            repalceFragment(new Profile());
                            break;
                    }
                    return true;
                });
    }

    public void repalceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();

    }
    public void hideBottomNavigationView() {
        binding.bottomNavigationView.setVisibility(View.GONE);
    }

    public void showBottomNavigationView() {
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
    }

}