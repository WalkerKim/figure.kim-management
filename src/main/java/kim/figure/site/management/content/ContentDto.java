package kim.figure.site.management.content;

import kim.figure.site.common.content.ContentFormat;
import kim.figure.site.common.tag.Tag;
import lombok.*;

import java.time.Instant;
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

          private Long id;

          private String title;

          private String contentFormat;

          private String rawContent;

          private String renderedContent;

          private String description;

          private Instant createdAt;

          private Instant publishedAt;

          private List<String> ogKeywordList;

          private List<Tag> tagList;

          private List<String> categoryIdList;

          //ogTag image
          String ogImage;

          Boolean isPublished;
     }


     @Getter
     @Setter
     @ToString
     @AllArgsConstructor
     @NoArgsConstructor
     public static class Put{

          private Long id;

          private String title;

          private ContentFormat contentFormat;

          private String rawContent;

          private String renderedContent;

          private String description;

          private Instant publishedAt;

          private List<String> ogKeywordList;

          private Long categoryId;

          //ogTag image
          private String ogImage;

          //og tag description
          private String ogDescription;

          private Boolean isPublished;

          private List<Tag> tagList;

          private List<String> categoryIdList;

//
//          public Long getId() {
//               return id;
//          }
//
//          public void setId(Long id) {
//               this.id = id;
//          }
     }

     @Getter
     @Setter
     @NoArgsConstructor
     public static class TempGet {
          private Long id;

          private String title;

          private ContentFormat contentFormat;

          private String rawContent;

          private String renderedContent;

          private String description;

          private Instant createdAt;

          private Boolean isDraft;

          private Instant lastModifiedAt;

          private Instant publishedAt;

          private List<String> ogKeywordList;

          private List<Tag> tagList;

          private List<String> categoryIdList;

          //ogTag image
          String ogImage;

          Boolean isPublished;

     }

     @Getter
     @AllArgsConstructor
     @NoArgsConstructor
     @Builder
     public static class TagRef{
          Long id;
          String title;
     }
}
