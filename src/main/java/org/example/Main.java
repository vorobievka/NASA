package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();


        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=xY3I4R3Aohw6L5oeg3DP4JbHp3tpubKA4XcAmQGt");

        String result = null;
        try {
            CloseableHttpResponse response = httpClient.execute(request);
//            System.out.println(response);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                result = new BufferedReader(new InputStreamReader(instream))
                        .lines().collect(Collectors.joining("\n"));
                ((InputStream) instream).close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        NASA nasa = jsonToObject(result);
        String url = nasa.getURL();
        System.out.println(nasa.getURL());

        HttpGet getImage = new HttpGet(url);
        HttpEntity image;
        try {
            CloseableHttpResponse res = httpClient.execute(getImage);
            image = res.getEntity();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String path = url.substring(url.lastIndexOf("/") + 1);

        saveImage(path, image);

    }

    public static void saveImage(String path, HttpEntity image) {
        InputStream is = null;
        try {
            is = image.getContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int inByte;
        while (true) {
            try {
                if (!((inByte = is.read()) != -1)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                fos.write(inByte);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static NASA jsonToObject(String jsonNew) {
        JSONParser parser = new JSONParser();
        JSONArray message = null;
        NASA nasa = null;
        try {
            Object obj = parser.parse(jsonNew);
            JSONObject jsonObject = (JSONObject) obj;
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            nasa = gson.fromJson(String.valueOf(jsonObject), NASA.class);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }
        return nasa;
    }

}