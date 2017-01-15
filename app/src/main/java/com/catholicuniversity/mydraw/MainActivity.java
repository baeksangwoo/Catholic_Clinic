package com.catholicuniversity.mydraw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText id,pw;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = (EditText) findViewById(R.id.editID);
        pw = (EditText) findViewById(R.id.editPW);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DrawMainActivity.class);// 내 java
                startActivity(intent);
                /*
                if (id.getText().toString().equals("clinic")) {
                    Intent intent = new Intent(MainActivity.this, DrawMainActivity.class);// 내 java
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "로그인 실패 !", Toast.LENGTH_SHORT).show();
                }
                */
            }
        });
    }
}