package kim.figure.site.management.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * author         : walker
 * date           : 2022. 12. 06.
 * description    :
 */
@Service
public class ImageService {

    @Value("${project.static.path}")
    String projectStaticPath;
    public Mono<String> imageStore(FilePart imageFilePart, String id) {
        String fileName = imageFilePart.filename();
        String timestamp = Long.toString(System.currentTimeMillis());
        if(fileName.equals("image.png")){
            fileName = timestamp+".png";
        }else{
            int extensionDotIndex = fileName.lastIndexOf(".");
            String extension = fileName.substring(extensionDotIndex);
            String originalFileName = fileName.substring(0, extensionDotIndex-1);
            fileName = originalFileName + timestamp + extension;
        }
        final String imageDir = projectStaticPath + "/assets/image/" + id;
        final String imagePath = projectStaticPath + "/assets/image/" + id + "/" + fileName;
        final String imageUri = "/assets/image/" + id + "/" + fileName;
        try {
            Files.createDirectories(Path.of(imageDir));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageFilePart.transferTo(Path.of(imagePath))
                        .thenReturn(imageUri);
    }
}
