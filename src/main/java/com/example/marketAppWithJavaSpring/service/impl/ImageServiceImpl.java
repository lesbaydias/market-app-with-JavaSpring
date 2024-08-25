package com.example.marketAppWithJavaSpring.service.impl;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageServiceImpl {
    String upload(MultipartFile image) throws IOException;
}
