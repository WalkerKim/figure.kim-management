package kim.figure.site.management.category;

import kim.figure.site.common.category.Category;
import kim.figure.site.common.content.Content;
import kim.figure.site.management.content.ContentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category categoryPostToEntity(CategoryDto.Post postCategory);
    Category categoryPutToEntity(CategoryDto.Put postCategory);

}
