package com.pvt73.recycling.controller;

import com.pvt73.recycling.exception.ErrorMessage;
import com.pvt73.recycling.model.dao.CleaningStatus;
import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.model.dao.LitteredPlace;
import com.pvt73.recycling.model.service.littered_place.LitteredPlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.List;
import java.util.Set;

@Tag(name = "Littered places", description = "Littered places to be cleaned.")
@Validated
@RequiredArgsConstructor
@RestController
public class LitteredPlaceController {
    private static final int OFFSET_MIN = 0;
    private static final int LIMIT_MIN = 1;
    private final LitteredPlaceService service;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New littered place Created."),
            @ApiResponse(responseCode = "400", description = "parameter is missing or wrong formatted.", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Littered place already exist.", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))})

    @PostMapping(value = "/littered-places")
    @ResponseStatus(HttpStatus.CREATED)
    LitteredPlace creat(@RequestBody @Valid LitteredPlace newLitteredPlace) {
        return service.creat(newLitteredPlace);
    }

    @Operation(summary = "One littered place, including a cleaned one.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Littered place found."),
            @ApiResponse(responseCode = "404", description = "Littered place not found.", content = @Content(schema = @Schema(implementation = ErrorMessage.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})

    @GetMapping(value = "/littered-places/{place-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    LitteredPlace findById(@PathVariable("place-id") int id,
                           @Parameter(description = "latitude,longitude")
                           @RequestParam @Size(min = 2, max = 2) double[] latlng) {

        return service.findById(id, new LatLng(latlng[0], latlng[1]));
    }

    @Operation(summary = "All littered places")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing littered places returned", content = @Content(schema = @Schema(implementation = LitteredPlace.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "204", description = "No more littered places nearby were found.", content = @Content)})

    @GetMapping(value = "/littered-places")
    ResponseEntity<List<LitteredPlace>> findAllNearby(@Parameter(description = "latitude,longitude")
                                                      @RequestParam @Size(min = 2, max = 2) double[] latlng,
                                                      @Parameter(description = "The index of the first result to return.")
                                                      @RequestParam(defaultValue = "0") @Min(OFFSET_MIN) int offset,
                                                      @Parameter(description = "Maximum number of results to return. " +
                                                              "Use with limit to get the next page of search results.")
                                                      @RequestParam(defaultValue = "10") @Min(LIMIT_MIN) int limit) {


        List<LitteredPlace> litteredPlaceList = service.findAllNearby(new LatLng(latlng[0], latlng[1]), offset, limit);

        return litteredPlaceList.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                ResponseEntity.ok(litteredPlaceList);
    }

    @PutMapping(value = "/littered-places/{place-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    LitteredPlace update(@RequestBody @Valid LitteredPlace litteredPlace,
                         @PathVariable("place-id") int id) {

        if (litteredPlace.getCleaningStatus() == CleaningStatus.CLEAN &&
                (litteredPlace.getCleanedBy() == null ||
                        litteredPlace.getCleanedBy().isBlank()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cleanedBy must be provided");

        return service.update(litteredPlace, id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content, Littered place deleted."),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(schema = @Schema(implementation = ErrorMessage.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})

    @DeleteMapping("/littered-places/{place-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable("place-id") int id) {
        service.delete(id);
    }


    @Operation(summary = "Add image to littered place. The image will be compressed, converted to WebP format, and 1080 pixels in width, keeping the aspect ratio.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New image created."),
            @ApiResponse(responseCode = "400", description = "parameter is missing or wrong formatted.", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "415", description = "Wrong file type, only image file! " +
                    "Make sure you are using the right content type; the request body is not empty.", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))})

    @PostMapping(value = "littered-places/{place-id}/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Image> addImage(@PathVariable("place-id") int litteredPlaceId,
                                   @RequestParam MultipartFile file) {


        if (service.isNotImage(file))
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "file must not be empty or has another type than an image");

        Image uploadedImage = service.addImage(litteredPlaceId, file);

        return ResponseEntity.
                created(URI.create(uploadedImage.getUrl()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(uploadedImage);
    }


    @Operation(summary = "Delete image from littered place")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content, Image deleted."),
            @ApiResponse(responseCode = "404", description = "Image not found.", content = @Content(schema = @Schema(implementation = ErrorMessage.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})

    @DeleteMapping("/littered-places/{place-id}/images/{image-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> deleteImage(@PathVariable("place-id") int id,
                                     @PathVariable("image-id") String imageName) {

        service.deleteImage(id, imageName);

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Find image i littered place")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image found."),
            @ApiResponse(responseCode = "404", description = "Image not found.", content = @Content(schema = @Schema(implementation = ErrorMessage.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})

    @GetMapping(value = "/littered-places/{place-id}/images/{image-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Image findImageById(@PathVariable("place-id") int id, @PathVariable("image-id") String imageId) {
        return service.findImageById(id, imageId);
    }


    @Operation(summary = "Find all image i littered place")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image set returned"),
            @ApiResponse(responseCode = "404", description = "not image found.", content = @Content(schema = @Schema(implementation = ErrorMessage.class), mediaType = MediaType.APPLICATION_JSON_VALUE))})

    @GetMapping(value = "/littered-places/{place-id}/images/", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Set<Image>> findAllImage(@PathVariable("place-id") int id) {

        Set<Image> imageSet = service.findAllImage(id);

        if (imageSet.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " no images found");
        else
            return ResponseEntity.ok(imageSet);

    }


}
