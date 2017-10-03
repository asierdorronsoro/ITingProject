package com.example.usuario.integrationmaps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LoginSuccess extends AppCompatActivity {
    TextView email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        email = (TextView)findViewById(R.id.maila);
        Bundle bundle = getIntent().getExtras();
        email.setText("Email "+ bundle.getString("email"));

    }
}
