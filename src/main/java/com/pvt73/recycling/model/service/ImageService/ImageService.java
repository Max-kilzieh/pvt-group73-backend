package com.pvt73.recycling.model.service.ImageService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class ImageService {

    private final ImageRepository service;
    private final Cloudinary cloudinary;

    public ImageService(ImageRepository service, Cloudinary cloudinary) {
        this.service = service;
        this.cloudinary = cloudinary;
    }

    public Image saveImage(MultipartFile file) throws IOException {
        Image toSave = new Image(true, 11.11111, 22.333333, file.getContentType(), file.getBytes(), file.getOriginalFilename());
        return service.save(toSave);
    }

    public Image getImage(String name) {

        return service.findByName(name);
    }

    public void deleteAll() {
        service.deleteAll();
    }


    public String uploadFile(MultipartFile image) {
        try {
            File uploadedFile = convertMultiPartToFile(image);
            var uploadResult = cloudinary.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
            boolean isDeleted = uploadedFile.delete();

            if (isDeleted) {
                System.out.println("File successfully deleted");
                System.out.println(uploadResult);
            } else
                System.out.println("File doesn't exist");
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
