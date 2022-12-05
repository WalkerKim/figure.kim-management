package kim.figure.site.management.content;

import kim.figure.site.common.category.Category;
import kim.figure.site.common.content.ContentFormat;
import kim.figure.site.common.tag.Tag;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * author         : walker
 * date           : 2022. 11. 21.
 * description    :
 */
public class ContentDto {

     @Getter
     @Setter
     @Builder
     @ToString
     @AllArgsConstructor
     @NoArgsConstructor
     public static class Post{

          private String title;

          private String contentFormat;

          private String rawContent;

          private String renderedContent;

          private String description;

          private Instant createdAt;

          private Instant publishAt;

          private List<String> ogKeywordList;

          private Category category;

          private List<Tag> tagList;

          private List<Category> categoryList;

          //ogTag image
          String ogImage;

          Boolean isPublished;
     }

     public static class Put{

          private Long Id;

          private String title;

          private ContentFormat contentFormat;

          private String rawContent;

          private String renderedContent;

          private String description;

          private Instant publishAt;

          private List<String> ogKeywordList;

          private Long categoryId;

          //ogTag image
          private String ogImage;

          //og tag description
          private String ogDescription;

          private Boolean isPublished;

          public void setId(Long id) {
               Id = id;
          }
     }

}
