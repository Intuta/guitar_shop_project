package com.myproject.guitar_shop.utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class ImageService {

    private static final String PATH = "C:\\Users\\79046\\IdeaProjects\\guitar_shop\\target\\classes\\static\\images\\%s";

    public static void saveImageAs(String fileName, MultipartFile image) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(String.format(PATH, fileName))) {
            fileOutputStream.write(image.getBytes());
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
