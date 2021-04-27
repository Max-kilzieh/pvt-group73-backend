package com.pvt73.recycling.model.service.ImageService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.repository.ImageRepository;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
public class ImageService {

    private final ImageRepository service;
    private final Cloudinary cloudinary;

    public ImageService(ImageRepository service, Cloudinary cloudinary) {
        this.service = service;
        this.cloudinary = cloudinary;
    }


    public void delete(String id) {
        try {
            var result = cloudinary.uploader().destroy(id, ObjectUtils.asMap("invalidate", true));
            if (!result.containsValue("ok")){
                System.err.println("couldn't delete the image at Cloudinary with id: " + id);
                System.err.println(result);
            }

            service.deleteImageById(id);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Image uploadImage(int userId,
                             boolean isClean,
                             double latitude,
                             double longitud,
                             MultipartFile file) {

        try {
            File imageToUpload = convertMultipartFileToImage(file);
            var uploadResult = cloudinary.uploader().upload(imageToUpload, ObjectUtils.emptyMap());
            if (!imageToUpload.delete())
                System.err.println("Couldn't delete the temporary image at root (/)");

            if (uploadResult.containsValue("Error")) {
                System.err.println(uploadResult);
                throw new FileUploadException("Image couldn't be uploaded");
            }

            return service.save(getImage(userId, isClean, latitude, longitud, uploadResult));

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();

        }
    }

    private Image getImage(int userId, boolean isClean, double latitude, double longitud, Map<?, ?> uploadResult) {
        return new Image(userId,
                isClean,
                latitude, longitud,
                uploadResult.get("public_id").toString(),
                uploadResult.get("secure_url").toString());
    }

    private File convertMultipartFileToImage(MultipartFile image) throws IOException {

        File convImage = new File(Objects.requireNonNull(image.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convImage);
        fos.write(image.getBytes());
        fos.close();

        return convImage;
    }
}
