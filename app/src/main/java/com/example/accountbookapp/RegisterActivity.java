package com.example.accountbookapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.accountbookapp.database.UserDao;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        registerButton = findViewById(R.id.register_button);

        userDao = new UserDao(this);

        registerButton.setOnClickListener(v -> register());
    }

    private void register() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        // 验证用户名格式
        if (!isValidUsername(username)) {
            Toast.makeText(this, "用户名只能包含英文字母和数字", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(username, password);
        long id = userDao.addUser(user);
        if (id != -1) {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidUsername(String username) {
        String pattern = "^[a-zA-Z0-9]+$";
        return Pattern.matches(pattern, username);
    }
}