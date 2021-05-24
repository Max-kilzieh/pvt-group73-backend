package com.pvt73.recycling.model.service.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pvt73.recycling.model.dao.Image;
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
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;


    public void delete(@NonNull String imageId) {

        try {
            cloudinary.uploader().destroy(imageId, ObjectUtils.asMap("invalidate", true));

        } catch (IOException e) {
            log.error("Couldn't delete the image with Id: " + imageId, e);
        }
    }

    @Override
    public void deleteAll(@NonNull Set<Image> imageSet) {
        for (Image image : imageSet) {

            delete(image.getId());
        }
    }


    public boolean isNotImage(MultipartFile file) {
        return (file == null ||
                file.isEmpty() ||
                file.getContentType() == null ||
                !file.getContentType().startsWith("image/"));

    }

    public Image creat(@NonNull MultipartFile file) {

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

        return getImage(uploadResult);
    }

    private Image getImage(Map<?, ?> uploadResult) {
        return new Image(

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
