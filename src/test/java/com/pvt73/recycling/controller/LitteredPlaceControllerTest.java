package com.pvt73.recycling.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pvt73.recycling.model.dao.CleaningStatus;
import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.LitteredPlace;
import com.pvt73.recycling.model.service.littered_place.LitteredPlaceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileInputStream;
import java.util.*;

import static com.pvt73.recycling.controller.ResponseBodyMatchers.responseBody;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LitteredPlaceController.class)
class LitteredPlaceControllerTest {
    private final LatLng kistaStation = new LatLng(59.40332696500667, 17.942350268367566);

    private final LitteredPlace litteredPlace = new LitteredPlace(kistaStation, "test@pvt.com");


    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LitteredPlaceService service;


    @Test
    void creatLitteredPlaceReturn201() throws Exception {


        given(service.creat(litteredPlace))
                .willReturn(litteredPlace);

        mvc.perform(post("/littered-places")
                .content(objectMapper.writeValueAsString(litteredPlace))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(responseBody().containsObjectAsJson(litteredPlace, LitteredPlace.class));


    }

    @Test
    void findLitteredPlaceByIdReturn200() throws Exception {
        given(service.findById(0, litteredPlace.getCoordinates()))
                .willReturn(litteredPlace);

        mvc.perform(get("/littered-places/0")
                .param("latlng", litteredPlace.getCoordinates().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(litteredPlace, LitteredPlace.class));
    }

    @Test
    void findAllNearbyCleaningStatus() throws Exception {

        final LitteredPlace placeOne = new LitteredPlace(new LatLng(59.123, 17.456), "test@pvt.com");
        placeOne.setCleaningStatus(CleaningStatus.NOT_CLEAN);
        final LitteredPlace placeTwo = new LitteredPlace(new LatLng(59.1234, 17.4567), "test@pvt.com");
        placeTwo.setCleaningStatus(CleaningStatus.NOT_CLEAN);

        List<LitteredPlace> placeList = Arrays.asList(placeOne, placeTwo);

        given(service.findAllNearbyCleaningStatus(CleaningStatus.NOT_CLEAN, new LatLng(59.1, 17.2), 0, 2))
                .willReturn(placeList);

        mvc.perform(get("/littered-places")
                .param("status", "NOT_CLEAN")
                .param("latlng", "59.1,17.2")
                .param("offset", "0")
                .param("limit", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));


    }

    @Test
    void findAllNearbyCleaningStatusReturnNoContent() throws Exception {

        given(service.findAllNearbyCleaningStatus(CleaningStatus.NOT_CLEAN, new LatLng(59.1, 17.2), 0, 2))
                .willReturn(Collections.emptyList());

        mvc.perform(get("/littered-places")
                .param("status", "NOT_CLEAN")
                .param("latlng", "59.1,17.2")
                .param("offset", "0")
                .param("limit", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    @Test
    void updateLitteredPlace() throws Exception {
        LitteredPlace after = new LitteredPlace(kistaStation, "test@pvt.com");
        after.setCleaningStatus(CleaningStatus.CLEAN);
        after.setCleanedBy("userId");
        after.setEvent(true);
        after.setLitteredImageSet(Set.of(new Image("notCleanId", "url", false)));
        after.setCleanedImageSet(Set.of(new Image("cleanId", "url", true)));


        given(service.update(after, 0)).willReturn(after);

        mvc.perform(put("/littered-places/0")
                .content(objectMapper.writeValueAsString(after))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(after, LitteredPlace.class));


    }

    @Test
    void updateLitteredPlaceToCleanWithoutCleanByReturnBadRequest() throws Exception {
        LitteredPlace after = new LitteredPlace(kistaStation, "test@pvt.com");
        after.setCleaningStatus(CleaningStatus.CLEAN);
        after.setEvent(true);
        after.setLitteredImageSet(Set.of(new Image("notCleanId", "url", false)));
        after.setCleanedImageSet(Set.of(new Image("cleanId", "url", true)));


        given(service.update(after, 0)).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "cleanedBy must be provided"));

        mvc.perform(put("/littered-places/0")
                .content(objectMapper.writeValueAsString(after))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"cleanedBy must be provided\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));


    }


    @Test
    void deleteLitteredPlace() throws Exception {
        mvc.perform(delete("/littered-places/0"))
                .andExpect(status().isNoContent());

    }

    @Test
    void uploadImageToLitteredPlace() throws Exception {
        final Image image = new Image("imageId", "https://res.cloudinary.com/pvt73/image/upload/", false);
        MockMultipartFile imageFile = new MockMultipartFile("file", "ImageControllerTest.jpg", "image/jpg", new FileInputStream("src/test/java/com/pvt73/recycling/controller/LitteredPlaceControllerTest.jpg"));

        given(service.addImage(0, imageFile, false)).willReturn(image);


        mvc.perform(multipart("/littered-places/0/images").file(imageFile).param("clean", "false"))
                .andExpect(status().isCreated())
                .andExpect(responseBody().containsObjectAsJson(image, Image.class));


    }

    @Test
    void UploadWrongFileTypeAndReturnUnsupportedMediaType() throws Exception {

        MockMultipartFile textFile = new MockMultipartFile("file", "ImageControllerTest.txt", "text/plain", new FileInputStream("src/test/java/com/pvt73/recycling/controller/LitteredPlaceControllerTest.txt"));

        given(service.isNotImage(textFile)).willReturn(true).willThrow(new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only image file"));

        mvc.perform(multipart("/littered-places/0/images").file(textFile).param("clean", "false"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("415 UNSUPPORTED_MEDIA_TYPE \"file must not be empty or has another type than an image\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    void deleteImageReturnNoContent() throws Exception {
        final Image image = new Image("imageId", "https://res.cloudinary.com/pvt73/image/upload/", true);

        mvc.perform(delete("/littered-places/0/images/" + image.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void findImageByIdReturnImage() throws Exception {
        final Image image = new Image("imageId", "https://res.cloudinary.com/pvt73/image/upload/", true);


        given(service.findImageById(0, image.getId())).willReturn(image);

        mvc.perform(get("/littered-places/0/images/" + image.getId()))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(image, Image.class));

    }

    @Test
    void findAllImage() throws Exception {
        Image litteredImage = new Image("litteredId", "https://res.cloudinary.com/pvt73/image/upload/", false);
        Image cleanedImage = new Image("cleanId", "https://res.cloudinary.com/pvt73/image/upload/", true);
        Set<Image> imageSet = Set.of(litteredImage, cleanedImage);

        given(service.findAllImage(0)).willReturn(imageSet);

        mvc.perform(get("/littered-places/0/images")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));


    }

    @Test
    void findAllImageReturnNotFound() throws Exception {

        given(service.findAllImage(0)).willReturn(Collections.emptySet());

        mvc.perform(get("/littered-places/0/images")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"no images found\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));


    }


}