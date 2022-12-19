package kim.figure.site.management.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * author         : walker
 * date           : 2022. 12. 04.
 * description    :
 */
@Component
@Slf4j
public class ImageHandler {

    private String userDirectory = "/static/pictures/";

    @Autowired
    ImageService imageService;

    public Mono<ServerResponse> postImage(ServerRequest serverRequest) {
        return serverRequest.body(BodyExtractors.toMultipartData())
                .flatMap(parts -> {
                    Map<String, Part> partMap = parts.toSingleValueMap();
                    FilePart imageFilePart = (FilePart) partMap.get("image");
                    String id = ((FormFieldPart) partMap.get("id")).value();
                    return imageService.imageStore(imageFilePart, id);
                }).flatMap(resultUri-> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(Map.of("result", resultUri)), Map.class));
    }




}

