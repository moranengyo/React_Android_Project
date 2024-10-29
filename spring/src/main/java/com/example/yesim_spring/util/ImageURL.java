package com.example.yesim_spring.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImageURL {

    public static String BASE_URL = "";
    public static String UPLOAD_URL = "";


//    @Value("${spring.web.resources.static-locations}")
//    private void setUploadPath(String url) {
//        UPLOAD_URL = url.replace("file:///", "");;
//    }

    @Value("${image-base-url}")
    public void setBaseUrl(String url){
        BASE_URL = url;
    }

    public static String thumbnail(String imgName) {

        return BASE_URL + imgName;
    }
}
