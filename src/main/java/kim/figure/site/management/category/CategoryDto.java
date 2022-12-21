package kim.figure.site.management.category;

import kim.figure.site.common.category.Category;
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

        private String parentCategoryId;
    }

    public class Put {

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Get{
        private String id;

        private String name;

        private String uriKey;

        private Integer depth;

        private List<Category> childCategoryList;

        private String parentCategoryId;
    }
}
