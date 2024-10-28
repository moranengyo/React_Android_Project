package com.example.yesim_spring.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImageURL {

    public static String BASE_URL = "";

    @Value("${image-base-url}")
    public void setBaseUrl(String url){
        BASE_URL = url;
    }

    public static String thumbnail(String imgName) {

        return BASE_URL + "image/" + imgName;
    }
}
