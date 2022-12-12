package com.myproject.guitar_shop.utility;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class TextFormatter {

    public static String formatText(String email) {
        return URLDecoder.decode(email, StandardCharsets.UTF_8).toLowerCase();
    }

}
