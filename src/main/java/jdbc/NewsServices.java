package jdbc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NewsServices {

    @Autowired
    NewsRepository newsRepository;

    public String uploadImage(MultipartFile file) {
        String imageUrl = null;
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uploadDir = "src/main/resources/static/images/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                imageUrl = "/images/" + fileName; // URL để truy cập hình ảnh từ frontend
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUrl;
    }

    public Post getPost(Long id) {
        Post post = newsRepository.findById(id);
        List<String> imageUrls = newsRepository.getImageUrlsByIdPost(id);
        post.setImageUrls(imageUrls);
        return post;
    }

    public List<String> getImageUrlsByIdPost(Long idPost){
        return newsRepository.getImageUrlsByIdPost(idPost);
    }

    public boolean deleteImage(String imageUrl) {
        String filePath = "src/main/resources/static/" + imageUrl;
        File imageFile = new File(filePath);
        // Check if the file exists
        if (!imageFile.exists()) {
            System.out.println("Image file does not exist");
            return false;
        }
        boolean deleted = imageFile.delete();
        return deleted;
    }

}
