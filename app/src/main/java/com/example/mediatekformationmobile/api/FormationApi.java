package com.example.mediatekformationmobile.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Classe d'accès à l'API
 */
public class FormationApi {

    // ⚠️ pas d'espaces, et doit finir par /
    private static final String API_URL = "https://mediatekformationmobile.alwaysdata.net/";

    private static Retrofit retrofit = null;

    /**
     * objet gson pour la conversion en json et le configure pour le format de dates
     */
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    /**
     * Construit l'objet unique qui permet d'accéder à l'api
     * @return retrofit
     */
    public static Retrofit getRetrofit() {
        if (retrofit == null) {

            // 1) Interceptor de logs HTTP
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // 2) Client OkHttp avec logging
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            // 3) Retrofit avec le client
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static Gson getGson() {
        return gson;
    }
}