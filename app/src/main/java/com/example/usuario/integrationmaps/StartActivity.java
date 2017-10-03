package com.example.usuario.integrationmaps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StartActivity extends AppCompatActivity {

    Button login_button;
    EditText izena, pasahitza;
    String Izena, Pasahitza;
    String login_url= "http://iting.es/php/login.php";
    AlertDialog.Builder builder;
    TextView olvido;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        textView = (TextView)findViewById(R.id.link_signup);
        textView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, Register.class ));
            }
        });
        builder = new AlertDialog.Builder(StartActivity.this);
        login_button = (Button)findViewById(R.id.btn_login);
        izena = (EditText)findViewById(R.id.correo);
        pasahitza = (EditText)findViewById(R.id.contra);



        login_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Izena = izena.getText().toString();
                Pasahitza = pasahitza.getText().toString();

                if(Izena.equals("")|| Pasahitza.equals("")){
                    builder.setTitle("Something went wrong");
                    displayAlert("Meter un usuario y contraseña validos");
                }else{
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url,
                            new Response.Listener<String>(){

                                @Override
                                public void onResponse(String response) {

                                    try {
                                        String intermedio = response.substring(16, response.length());
                                        JSONArray jsonArray = new JSONArray(intermedio);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        if(code.equals("Log in fallido")){
                                            builder.setTitle("Log in Error");
                                            displayAlert(jsonObject.getString("message"));
                                        }else{
                                            Intent intent = new Intent(StartActivity.this, MapsActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("email", jsonObject.getString("email"));
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(StartActivity.this, "Error", Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("email", Izena );
                            params.put("password", Pasahitza);
                            return params;
                        }
                    };
                    Mysingleton.getnInstance(StartActivity.this).addToRequestQue(stringRequest);
                }
            }
        });
    }
    public void displayAlert(String message){
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                izena.setText("");
                pasahitza.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
