package com.example.accountbookapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.accountbookapp.fragment.AddRecordFragment;
import com.example.accountbookapp.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.action_add) {
                replaceFragment(new AddRecordFragment());
                return true;
            } else if (itemId == R.id.action_stats) {
                replaceFragment(new com.example.accountingapp.fragment.StatisticsFragment());
                return true;
            }
            return false;
        });

        // 默认显示主页
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.action_home);
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}  