package com.wallhack.weathermap.Service;

import lombok.Getter;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class API_KEY_ACCESS {
    @Getter
    private static String apiKey;

    static {
        String Key = System.getenv("my_api_key");
        if (Key == null) {
            try (InputStream is = API_KEY_ACCESS.class.getResourceAsStream("/api-key.properties")){
                Properties prop = new Properties();
                prop.load(is);
                Key = prop.getProperty("my_api_key");
                apiKey = Key;
            }catch (IOException e){
               log.error(e.getMessage());
            }
        }
    }
}
