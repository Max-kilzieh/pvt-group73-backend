package com.pvt73.recycling.model.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.repository.ImageRepository;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
public class ImageService {

    private final ImageRepository repository;
    private final Cloudinary cloudinary;

    public ImageService(ImageRepository repository, Cloudinary cloudinary) {
        this.repository = repository;
        this.cloudinary = cloudinary;
    }


    public void delete(String id) {

        try {

            if (repository.existsById(id)) {
                cloudinary.uploader().destroy(id, ObjectUtils.asMap("invalidate", true));
                repository.deleteById(id);
            } else {
                throw new FileNotFoundException("Image with Id: " + id + " not found!");
            }

        } catch (IOException e) {
            System.err.println("Couldn't delete the image with Id: " + id);
            e.printStackTrace();
        }
    }

    public boolean isNotImage(MultipartFile file) {
        return (file == null ||
                file.isEmpty() ||
                file.getContentType() == null ||
                !file.getContentType().startsWith("image/"));

    }

    public Image uploadImage(int userId, boolean isClean, double latitude, double longitud, MultipartFile file) throws IOException {

            File imageToUpload = convertMultipartFileToImage(file);

            var uploadResult = cloudinary.uploader().upload(imageToUpload, ObjectUtils.emptyMap());

            if (!imageToUpload.delete())
                System.err.println("Couldn't delete the temporary image at root (/)");

            return repository.save(getImage(userId, isClean, latitude, longitud, uploadResult));

    }

    private Image getImage(int userId, boolean isClean, double latitude, double longitud, Map<?, ?> uploadResult) {
        return new Image(userId,
                isClean,
                latitude, longitud,
                uploadResult.get("public_id").toString(),
                uploadResult.get("secure_url").toString());
    }

    private File convertMultipartFileToImage(MultipartFile file) throws IOException {

        if (isNotImage(file))
            throw new IllegalArgumentException("Not an Image file!");

        File convImage = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convImage);
        fos.write(file.getBytes());
        fos.close();

        return convImage;
    }
}
