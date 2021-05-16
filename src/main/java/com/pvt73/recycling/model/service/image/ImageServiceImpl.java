package com.pvt73.recycling.model.service.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pvt73.recycling.exception.ResourceNotFoundException;
import com.pvt73.recycling.model.dao.Image;
import com.pvt73.recycling.model.dao.LatLng;
import com.pvt73.recycling.repository.ImageRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository repository;
    private final Cloudinary cloudinary;


    public void delete(@NonNull String id) {


        try {
            cloudinary.uploader().destroy(id, ObjectUtils.asMap("invalidate", true));
            repository.deleteById(id);

        } catch (IOException e) {
            log.error("Couldn't delete the image with Id: " + id, e);
            e.printStackTrace();
        }
    }

    @Override
    public Image findById(@NonNull String id) {

        return repository.findById(id)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("id", id, "image not found.");
                });
    }

    public boolean isNotImage(MultipartFile file) {
        return (file == null ||
                file.isEmpty() ||
                file.getContentType() == null ||
                !file.getContentType().startsWith("image/"));

    }

    public Image creat(int userId, boolean clean, @NonNull LatLng coordinates, String description, @NonNull MultipartFile file) {
        Map<?, ?> uploadResult = null;
        try {

            File imageToUpload = convertMultipartFileToImage(file);

            uploadResult = cloudinary.uploader().upload(imageToUpload, ObjectUtils.emptyMap());

            if (!imageToUpload.delete())
                log.error("Couldn't delete the temporary image at root (/)");

        } catch (IOException e) {
            e.printStackTrace();
        }


        assert uploadResult != null;
        return repository.save(getImage(userId, clean, coordinates, description, uploadResult));
    }

    private Image getImage(int userId, boolean clean, LatLng coordinates, String description, Map<?, ?> uploadResult) {
        return new Image(userId,
                clean, coordinates,
                description,
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
