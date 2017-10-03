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

public class Register extends AppCompatActivity {

    Button reg_bn;
    EditText nombre, email, password, confirm_password;
    String Nombre, Email, Password, Confirm_Pass;
    TextView link_login;
    AlertDialog.Builder builder;
    String reg_url = "http://iting.es/php/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //-----------------------------------------

        reg_bn = (Button)findViewById(R.id.btn_signup);
        nombre = (EditText)findViewById(R.id.input_name);
        email = (EditText)findViewById(R.id.input_email);
        password = (EditText)findViewById(R.id.input_password);
        confirm_password = (EditText)findViewById(R.id.input_confirm_password);
        link_login = (TextView)findViewById(R.id.link_login);

        builder = new AlertDialog.Builder(Register.this);

        reg_bn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Nombre= nombre.getText().toString();
                Email= email.getText().toString();
                Password= password.getText().toString();
                Confirm_Pass= confirm_password.getText().toString();

                if(Nombre.equals("")||Email.equals("")||Password.equals("")||Confirm_Pass.equals("")){
                    builder.setTitle("Algo fue mal...");
                    builder.setMessage("Por favor, rellene todos los campos");
                    displayAlert("input_error");
                }else{
                    if(!(Password.equals(Confirm_Pass))){
                        builder.setTitle("Algo fue mal...");
                        builder.setMessage("Las contraseñas no coinciden");
                        displayAlert("input_error");
                    }else{
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, reg_url,
                                new Response.Listener<String>(){

                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            String intermedio = response.substring(16, response.length());
                                            JSONArray jsonArray = new JSONArray(intermedio);
                                            JSONObject jsonObject =  jsonArray.getJSONObject(0);
                                            System.out.println("********************"+response);
                                            String code = jsonObject.getString("code");
                                            String message = jsonObject.getString("message");
                                            builder.setTitle("Server response...");
                                            builder.setMessage(message);
                                            displayAlert(code);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener(){

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Register.this, "Error", Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("nombre", Nombre);
                                params.put("email", Email );
                                params.put("password", Password );
                                return params;
                            }
                        };
                        Mysingleton.getnInstance(Register.this).addToRequestQue(stringRequest);
                    }
                }
            }
        });

        link_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, StartActivity.class ));
            }
        });
    }

    public void displayAlert(final String code){
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(code.equals("input_error")){
                    password.setText("");
                    confirm_password.setText("");
                } else if (code.equals("registro exitoso")) {
                    finish();
                }else if(code.equals("registro fallido")){
                    nombre.setText("");
                    email.setText("");
                    password.setText("");
                    confirm_password.setText("");
                }

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
