package kim.figure.site.management.category;

import kim.figure.site.common.category.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    Category categoryPostToEntity(CategoryDto.Post postCategory);
    Category categoryPutToEntity(CategoryDto.Put postCategory);
    CategoryDto.Get categoryToGet(Category category);

}
