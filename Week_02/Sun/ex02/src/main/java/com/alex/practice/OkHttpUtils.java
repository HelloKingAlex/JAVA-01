package com.alex.practice;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author : Alex Shen
 * @className : OkHttpUtils
 * @description :
 * @date: 2021-01-19 22:34
 * @version: 1.0.0
 */
public class OkHttpUtils {
    public static OkHttpClient client = new OkHttpClient();

    public static String getAsString(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void main(String[] args) throws Exception {
        String url = "http://localhost:8808";
        String text = OkHttpUtils.getAsString(url);
        System.out.println("url:" + url + "; response: \n" + text);

    }
}
