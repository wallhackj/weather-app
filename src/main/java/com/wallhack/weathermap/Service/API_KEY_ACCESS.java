package com.wallhack.weathermap.Service;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class API_KEY_ACCESS {
    @Getter
    private static String apiKey;
    private static final Logger LOGGER = LogManager.getLogger(API_KEY_ACCESS.class);

    static {
        String Key = System.getenv("my_api_key");
        if (Key == null) {
            try (InputStream is = API_KEY_ACCESS.class.getResourceAsStream("/api-key.properties")){
                Properties prop = new Properties();
                prop.load(is);
                Key = prop.getProperty("my_api_key");
                apiKey = Key;
            }catch (IOException e){
                LOGGER.error(e);
            }
        }
    }
}
