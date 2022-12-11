package com.myproject.guitar_shop.utility;

import java.util.HashMap;
import java.util.Map;

public class RequestMapper {

    public static Map<String, String> mapRequestBody(String body) {
        Map<String, String> data = new HashMap<>();
        String[] split = body.split("&");
        for (String s : split) {
            String[] pair = s.split("=");
            data.put(pair[0], pair[1]);
        }
        return data;
    }
}