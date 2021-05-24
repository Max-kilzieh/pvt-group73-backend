//package com.pvt73.recycling.controller;
//
//import com.pvt73.recycling.model.dao.Image;
//import com.pvt73.recycling.model.dao.LatLng;
//import com.pvt73.recycling.model.service.image.ImageService;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.io.FileInputStream;
//import java.util.Objects;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(ImageController.class)
//class ImageControllerTest {
//    private static final Image image = new Image("userId", true, new LatLng(1.1, 2.2), "desc", "imageId", "https://res.cloudinary.com/pvt73/image/upload/");
//    private static final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
//
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private ImageService service;
//
//    @BeforeAll
//    static void beforeAll() {
//        setParameters();
//    }
//
//    private static void setParameters() {
//        parameters.add("userId", "userId");
//        parameters.add("clean", "true");
//        parameters.add("latitude", "1.1");
//        parameters.add("longitude", "2.2");
//        parameters.add("description", "desc");
//
//    }
//
//    @Test
//    void uploadImage() throws Exception {
//
//        MockMultipartFile imageFile = new MockMultipartFile("file", "ImageControllerTest.jpg", "image/jpg", new FileInputStream("src/test/java/com/pvt73/recycling/controller/ImageControllerTest.jpg"));
//
//        given(service.creat("userId", true, new LatLng(1.1, 2.2), "desc", imageFile)).willReturn(image);
//
//
//        mvc.perform(multipart("/images").file(imageFile).params(parameters))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("imageId").value("imageId"))
//                .andExpect(jsonPath("userId").value("userId"))
//                .andExpect(jsonPath("url").value("https://res.cloudinary.com/pvt73/image/upload/"))
//                .andExpect(jsonPath("coordinates.latitude").value("1.1"))
//                .andExpect(jsonPath("coordinates.longitude").value("2.2"))
//                .andExpect(jsonPath("description").value("desc"))
//                .andExpect(jsonPath("clean").value("true"));
//
//    }
//
//    @Test
//    void UploadWrongFileTypeAndReturnUnsupportedMediaType() throws Exception {
//
//        MockMultipartFile textFile = new MockMultipartFile("file", "ImageControllerTest.txt", "text/plain", new FileInputStream("src/test/java/com/pvt73/recycling/controller/ImageControllerTest.txt"));
//
//        given(service.isNotImage(textFile)).willReturn(true).willThrow(new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only image file"));
//
//        mvc.perform(multipart("/images").file(textFile).params(parameters))
//                .andExpect(status().isUnsupportedMediaType())
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
//                .andExpect(result -> assertEquals("415 UNSUPPORTED_MEDIA_TYPE \"file must not be empty or has another type than an image\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
//
//    }
//
//    @Test
//    void findByIdReturnImage() throws Exception {
//
//
//        given(service.findById(image.getId())).willReturn(image);
//
//        mvc.perform(MockMvcRequestBuilders.get("/images/" + image.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("imageId").value("imageId"))
//                .andExpect(jsonPath("userId").value("userId"))
//                .andExpect(jsonPath("url").value("https://res.cloudinary.com/pvt73/image/upload/"))
//                .andExpect(jsonPath("coordinates.latitude").value("1.1"))
//                .andExpect(jsonPath("coordinates.longitude").value("2.2"))
//                .andExpect(jsonPath("description").value("desc"))
//                .andExpect(jsonPath("clean").value("true"));
//
//    }
//
//
//    @Test
//    void deleteReturnNoContent() throws Exception {
//        mvc.perform(MockMvcRequestBuilders.delete("/images/imageId"))
//                .andExpect(status().isNoContent());
//    }
//}
