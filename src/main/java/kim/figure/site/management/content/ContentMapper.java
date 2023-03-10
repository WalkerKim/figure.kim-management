package kim.figure.site.management.content;

import kim.figure.site.common.content.Content;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContentMapper {
    ContentMapper INSTANCE = Mappers.getMapper(ContentMapper.class);

    Content contentPostToEntity(ContentDto.Post postContent);
    Content contentPutToEntity(ContentDto.Put postContent);

    ContentDto.TempGet contentEntityToGet(Content content);
}
