package com.example.usuario.integrationmaps;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Usuario on 10/09/2017.
 */

public class Mysingleton {

    private static Mysingleton nInstance;
    private RequestQueue requestQueue;
    private static Context context;

    private Mysingleton(Context cont){

        context = cont;
        requestQueue = getRequestQueue();
    }


    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized Mysingleton getnInstance(Context cont){

        if(nInstance==null){
            nInstance = new Mysingleton(cont);
        }
        return nInstance;
    }

    public <T> void addToRequestQue(Request<T> request){
        System.out.println("QUE COÑO ESTÁ PASANDO");
        requestQueue.add(request);
    }

}
