package com.example.lenovo.materialdesign.json;

import com.example.lenovo.materialdesign.MyApplication;
import com.example.lenovo.materialdesign.extras.UrlEndpoints;

/**
 * Created by lenovo on 1/7/2016.
 */
public class EndPoints {
    public static String getRequestUrl(int limit) {
        return UrlEndpoints.URL_BOX_OFFICE + UrlEndpoints.URL_CHAR_QUESTION + UrlEndpoints.URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES + UrlEndpoints.URL_CHAR_AMEPERSAND + UrlEndpoints.URL_PARAM_LIMIT + limit;
    }
}
