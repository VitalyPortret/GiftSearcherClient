package com.giftsearcher.giftsearcherclient.util;

import com.alibaba.fastjson.JSON;
import com.giftsearcher.giftsearcherclient.entity.Gift;
import com.giftsearcher.giftsearcherclient.entity.Shop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import java.util.List;
import android.util.Log;

public class JSONUtil {

    public static List<Gift> getGiftListFromJSON(String stringUrl) throws IOException {
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
    }

    public static Gift getGiftFromJSON(String stringUrl) throws IOException {
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
    }

    public static Shop getShopFromJSON(String stringUrl) throws IOException {
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
            connection.setDoInput(true);
            connection.connect();

            inputStream  = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            String line = "";
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return JSON.parseObject(stringBuilder.toString(), Shop.class);
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
    }

    public static List<Gift> getGiftsFromAdvancedSearch(String... args) throws IOException {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        BufferedReader reader = null;
        URL url;

        String params = "priceFrom=" + args[1] +
                "&priceTo="+ args[2] +
                "&ageFrom="+ args[3] +
                "&ageTo="+ args[4] +
                "&gender="+ args[5] +
                "&hobby="+ args[6] +
                "&holiday="+ args[7];

        try {
            url = new URL(args[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestProperty("Content-Length", "" + Integer.toString(params.getBytes().length));
            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            byte[] data = params.getBytes("UTF-8");
            os.write(data);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                inputStream  = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return JSON.parseArray(stringBuilder.toString(), Gift.class);
            }
            return null;
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
    }
}
