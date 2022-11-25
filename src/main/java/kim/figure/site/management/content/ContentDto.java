package kim.figure.site.management.content;

import kim.figure.site.common.content.ContentFormat;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * author         : walker
 * date           : 2022. 11. 21.
 * description    :
 */
public class ContentDto {

     public static class Post{

          private String title;

          private ContentFormat contentFormat;

          private String rawContent;

          private String renderedContent;

          private String description;

          private Boolean draft = true;

          private ZonedDateTime publishAt;

          private List<String> ogKeywordList;

          private Long categoryId;

          //ogTag image
          private String ogImage;

          //og tag description
          private String ogDescription;

          private Boolean isPublished;
     }

     public static class Put{

          private Long Id;

          private String title;

          private ContentFormat contentFormat;

          private String rawContent;

          private String renderedContent;

          private String description;

          private Boolean draft = true;

          private ZonedDateTime publishAt;

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
