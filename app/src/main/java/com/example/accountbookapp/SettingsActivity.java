package com.example.accountbookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // 返回按钮点击事件
        findViewById(R.id.back).setOnClickListener(v -> {
            finish(); // 返回上一级页面
        });
    }
}