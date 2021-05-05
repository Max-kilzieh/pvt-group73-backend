package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.model.service.ImageService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


@RestController()
@Tag(name = "Images")
public class ImageController {
    private final ImageService service;

    public ImageController(ImageService service) {
        this.service = service;
    }

    @Operation(summary = "Upload image to cloud")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New image created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Image.class))}),
            @ApiResponse(responseCode = "415", description = "Wrong file type, only image file! Make sure you are using the right content type; the request body is not empty.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "The parameter 'file' is not present.",
                    content = @Content(mediaType = "application/json"))})
    @PostMapping(value = "/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)


    public ResponseEntity<Image> uploadImage(@Parameter(description = "Only one image file")
                                             @RequestParam MultipartFile file) {

        if (service.isNotImage(file))
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only image file");

        Image uploadedImage = service.uploadImage(1, true, 1.1111, 2.2222, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(uploadedImage);
    }

    @Operation(summary = "Delete image from cloud")


    @DeleteMapping("/images/{id}")
    public void delete(@Parameter(description = "The image name is the Id.")
                       @PathVariable String id) {

        service.delete(id);
    }
}
