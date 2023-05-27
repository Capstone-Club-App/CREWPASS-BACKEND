package Capstone.Crewpass.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatGptConfig {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static String KEY;
    @Value("${chatGPT.API.key}")
    public void setKey(String value) {
        KEY = value;
    }
    public static final String MODEL = "text-davinci-003";
    public static final Integer MAX_TOKEN = 1500;
    public static final Double TEMPERATURE = 0.0;
    public static final Double TOP_P = 1.0;
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String URL = "https://api.openai.com/v1/completions";

}