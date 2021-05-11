package com.pvt73.recycling.controller;

import com.pvt73.recycling.exception.RestResponse;
import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.model.service.imageService.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;


@RestController
@Tag(name = "Images",
        description = "Handling image compression, upload, download, and conversion to size 1080 pixels width, keeping the aspect ratio.")
@Validated
public class ImageController {
    private final ImageService service;

    public ImageController(ImageService service) {
        this.service = service;
    }

    @Operation(summary = "Upload image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New image created"),
            @ApiResponse(responseCode = "400", description = "One or more parameters are missing or wrong formatted.", content = @Content(schema = @Schema(implementation = RestResponse.class))),
            @ApiResponse(responseCode = "415", description = "Wrong file type, only image file! " +
                    "Make sure you are using the right content type; the request body is not empty.", content = @Content(schema = @Schema(implementation = RestResponse.class)))})


    @PostMapping(value = "/images",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<Image> uploadImage(@RequestParam int userId,
                                             @RequestParam boolean clean,
                                             @RequestParam double latitude,
                                             @RequestParam double longitude,
                                             @RequestParam(required = false) String description,
                                             @Parameter(description = "Only one image file")
                                             @RequestParam MultipartFile file) {


        if (service.isNotImage(file))
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "file must not be empty or has another type than an image");

        Image uploadedImage;

        try {
            uploadedImage = service.uploadImage(userId, clean, latitude, longitude, description, file);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server encountered an error");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(uploadedImage);
    }


    @Operation(summary = "Delete image")
    @DeleteMapping("/images/{id}")
    public void delete(@Parameter(description = "The image name is the Id.")
                       @PathVariable String id) {

        service.delete(id);
    }

}
