package kim.figure.site.management.category;

import kim.figure.site.common.category.Category;
import kim.figure.site.common.content.Content;
import lombok.*;

import java.util.List;

/**
 * author         : walker
 * date           : 2022. 12. 01.
 * description    :
 */
public class CategoryDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post{

        private String id;

        private String name;

        private String uriKey;

        private Integer depth;

        private List<String> childCategoryIdList;

        private String parentCategoryId;

        private List<String> contentIdList;
    }

    public class Put {

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Get{
        private String id;

        private String name;

        private String uriKey;

        private Integer depth;

        private List<CategoryDto.Get> childCategoryList;

        private String parentCategoryId;

        private List<Content> contentList;
    }
}
