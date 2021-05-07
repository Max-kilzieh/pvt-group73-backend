package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.model.service.ImageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
class ImageControllerTest {
    private static final Image image = new Image(0, true, 1.1, 2.2, "imageId", "https://res.cloudinary.com/pvt73/image/upload/");
    private static final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();


    @Autowired
    private MockMvc mvc;

    @MockBean
    private ImageService service;

    @BeforeAll
    static void beforeAll() {
        setParameters();
    }

    private static void setParameters() {
        parameters.add("userId", "0");
        parameters.add("isClean", "true");
        parameters.add("latitude", "1.1");
        parameters.add("longitud", "2.2");
    }

    @Test
    void uploadImage() throws Exception {

        MockMultipartFile imageFile = new MockMultipartFile("file", "ImageControllerTest.jpg", "image/jpg", new FileInputStream("src/test/java/com/pvt73/recycling/controller/ImageControllerTest.jpg"));

        given(service.uploadImage(0, true, 1.1, 2.2, imageFile)).willReturn(image);


        mvc.perform(multipart("/images").file(imageFile).params(parameters))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value("imageId"))
                .andExpect(jsonPath("userId").value("0"))
                .andExpect(jsonPath("url").value("https://res.cloudinary.com/pvt73/image/upload/"))
                .andExpect(jsonPath("latitude").value("1.1"))
                .andExpect(jsonPath("longitude").value("2.2"))
                .andExpect(jsonPath("clean").value("true"));

    }

    @Test
    void UploadIOExecution() throws Exception {

        MockMultipartFile textFile = new MockMultipartFile("file", "ImageControllerTest.txt", "text/plain", new FileInputStream("src/test/java/com/pvt73/recycling/controller/ImageControllerTest.txt"));

        given(service.uploadImage(0, true, 1.1, 2.2, textFile)).willThrow(new IOException());


        mvc.perform(multipart("/images").file(textFile).params(parameters))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("500 INTERNAL_SERVER_ERROR \"Please contact Max\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    void UploadWrongFileType() throws Exception {

        MockMultipartFile textFile = new MockMultipartFile("file", "ImageControllerTest.txt", "text/plain", new FileInputStream("src/test/java/com/pvt73/recycling/controller/ImageControllerTest.txt"));

        given(service.isNotImage(textFile)).willReturn(true).willThrow(new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only image file"));

        mvc.perform(multipart("/images").file(textFile).params(parameters))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("415 UNSUPPORTED_MEDIA_TYPE \"Only image file\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/images/imageId"))
                .andExpect(status().isOk());
    }
}