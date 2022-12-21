package kim.figure.site.management.tag;

import kim.figure.site.management.content.ContentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * author         : walker
 * date           : 2022. 12. 20.
 * description    :
 */
public class TagDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Get{
        String id;
        List<ContentDto.TagRef> contentList;
    }
}
