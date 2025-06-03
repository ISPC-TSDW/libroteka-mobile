package com.example.libroteka.retrofit;

import java.io.IOException;
import java.util.HashMap;

import com.example.libroteka.SessionManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    private static final String BASE_URL = "http://10.0.2.2:8000/";
    //"https://libroback.koyeb.app/"; //"https://libroteka-app.onrender.com/";
    private static String authToken;

    //Método para actualizar el token de autenticación
    public static void setAuthToken(String token) {
        authToken = token;
        retrofit = null; // Reset retrofit para que se reinicialice con el nuevo token
    }
    //Superusuario12!
    public static Retrofit getRetrofitInstance(SessionManager sessionManager) {

        // Create a logging interceptor for monitoring requests and responses
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Logs request and response body

        // Build OkHttpClient with the interceptor
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder();
                        // Agregar el accessToken si está presente
                        HashMap<String, String> userDetails = sessionManager!=null && sessionManager.getUserDetails() != null?sessionManager.getUserDetails():null;
                        String accessToken = userDetails!= null?userDetails.get(SessionManager.KEY_TOKEN):null;
                        if (accessToken != null) {
                            requestBuilder.header("Authorization", "Bearer " + accessToken);
                        }
                        return chain.proceed(requestBuilder.build());
                    }
                })
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient) // Use OkHttpClient with the logging interceptor
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

