package com.giftsearcher.giftsearcherclient.util;

import com.alibaba.fastjson.JSON;
import com.giftsearcher.giftsearcherclient.entity.Gift;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.List;
import android.util.Log;

public class JSONUtil {

    public static List<Gift> getGiftListFromJSON(String stringUrl) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        BufferedReader reader = null;
        URL url;

        try {
            url = new URL(stringUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept-Encoding", "identity");
            connection.setDoInput(true);
            connection.connect();

            inputStream  = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            String line = "";
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return JSON.parseArray(stringBuilder.toString(), Gift.class);
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Gift getGiftFromJSON(String stringUrl) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        BufferedReader reader = null;
        URL url;

        try {
            url = new URL(stringUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept-Encoding", "identity");
            connection.setDoInput(true);
            connection.connect();

            inputStream  = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            String line = "";
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return JSON.parseObject(stringBuilder.toString(), Gift.class);
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
