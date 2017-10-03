package com.example.usuario.integrationmaps;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Usuario on 15/09/2017.
 */

public class ScrrenshotAdapter extends PagerAdapter{


    ArrayList<String> arrayList = new ArrayList<>();
    String devolver;
    String id;
    String number;

    /*public void rellenar(){
        arrayList.add("http:/iting.es/img/1/a.jpg");
        arrayList.add("http:/iting.es/img/1/b.jpg");
        arrayList.add("http:/iting.es/img/1/c.jpg");
    }
*/
    private void cuantos(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://iting.es/php/imagen_menu121.php",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject;
                            for(int i=0;i<jsonArray.length();i++){
                                jsonObject = jsonArray.getJSONObject(i);
                                arrayList.add("http://iting.es/img/"+id+"/"+jsonObject.getString("nombre"));
                                System.out.println(arrayList.get(i));
                            }
                            notifyDataSetChanged();

                            //String code = jsonObject.getString("count(*)");
                            //number = code;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Algo ha ido mal", Toast.LENGTH_LONG).show();
                error.printStackTrace();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurante_id", id );
                return params;
            }
        };


        Mysingleton.getnInstance(context).addToRequestQue(stringRequest);


    }
   /* private void rellenar(){

        String login_url= "http:/iting.es/php/imagen_menu.php";
        System.out.println("No entienod como no pasas hijodeputa");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, login_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        int count=0;

                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                String intermedio = "http:/iting.es/img/"+id+"/"+jsonObject.getString("nombre");
                                System.out.println(intermedio);
                                arrayList.add(intermedio);
                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("PASA POR FALLO");
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_restaurante", id );
                System.out.println("que coñooooooooooo"+id);
                return params;
            }
        };
        Mysingleton.getnInstance(context).addToRequestQue(jsonArrayRequest);


    }*/

    private LayoutInflater layoutInflater;
    private Context context;

    public ScrrenshotAdapter(Context context, String id) {
        this.context = context;
        this.id=id;
        cuantos();


    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }

    @Override
    public int getCount() {
        return arrayList.size();

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.scheenshots, container, false);
        ImageView imageView = (ImageView)item_view.findViewById(R.id.slider_image);



        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
            }
        });


        builder.build().load(arrayList.get(position)).resize(700,400).centerCrop().into(imageView);
        container.addView(item_view);
        return item_view;

    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
