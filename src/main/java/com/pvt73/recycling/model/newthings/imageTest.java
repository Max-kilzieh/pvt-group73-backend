package com.pvt73.recycling.model.newthings;

import org.springframework.web.multipart.MultipartFile;

public class imageTest {
    private String test;
    private MultipartFile multipartFile;

    public imageTest(String test, MultipartFile multipartFile) {
        this.test = test;
        this.multipartFile = multipartFile;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
