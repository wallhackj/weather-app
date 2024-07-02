package com.wallhack.weathermap.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class API_KEY_ACCESS {
    public static String apiKey;

    static {
        String Key = System.getenv("my_api_key");
        if (Key == null) {
            try (InputStream is = API_KEY_ACCESS.class.getResourceAsStream("/api-key.properties")){
                Properties prop = new Properties();
                prop.load(is);
                Key = prop.getProperty("my_api_key");
                apiKey = Key;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
