package kim.figure.site.management.tag;

import kim.figure.site.common.category.Category;
import kim.figure.site.management.category.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);
    Category categoryPostToEntity(CategoryDto.Post postCategory);
    Category categoryPutToEntity(CategoryDto.Put postCategory);
    CategoryDto.Get categoryToGet(Category category);

}
