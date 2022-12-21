package kim.figure.site.management.category;

import kim.figure.site.common.category.Category;
import kim.figure.site.management.content.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.Optional;

/**
 * author         : walker
 * date           : 2022. 12. 19.
 * description    :
 */
@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ContentRepository contentRepository;


    public Mono<Void> deleteCategory(String id) {
        return categoryRepository.findById(id).flatMap(category -> {
            if (category.getChildCategoryList()==null||category.getChildCategoryList().isEmpty()) {
            } else {
                return Mono.error(new RuntimeException("Child category exists. :" + category.getChildCategoryList()));
            }
            return contentRepository.findByCategoryList(category).flatMap(content -> {
                content.getCategoryList().remove(category);
                return contentRepository.save(content);
            }).then(categoryRepository.delete(category));
        });
//        Flux<Category> childCategoryFlux = categoryRepository.findByParentCategory(category);
//        return childCategoryFlux.collectList().flatMap(list -> {
//            if (list.isEmpty()) {
//                return categoryRepository.deleteById(id);
//            } else {
//                return Mono.error(new RuntimeException("Child category exists. :" + list));
//            }
//        });

    }


    public Flux<CategoryDto.Get> getCategoryFlux() {
        return categoryRepository.findByDepth(0).map(CategoryMapper.INSTANCE::categoryToGet);

//        return categoryRepository.findAll().groupBy(category -> category.getParentCategory()!=null?category.getParentCategory():category)
//                .flatMap(groupedFlux -> {
//                    var childMonoList = groupedFlux.collectList();
//                    var parentCategory = CategoryMapper.INSTANCE.categoryToGet(groupedFlux.key());
//                    var containChildCategory = childMonoList.map(childList -> {
//                        parentCategory.setChildCategoryList(childList.stream().filter(childCategory->parentCategory.getId()!=childCategory.getId()).map(CategoryMapper.INSTANCE::categoryToGet).map(childCategory -> {
//                            childCategory.setParentCategoryId(parentCategory.getId());
//                            return childCategory;
//                        }).toList());
//                        return parentCategory;
//                    });
//                    return containChildCategory;
//                });
    }

    public Mono<Category> postCategory(Mono<CategoryDto.Post> monoDto) {
        return monoDto.map(dto -> {
                    Category category = CategoryMapper.INSTANCE.categoryPostToEntity(dto);
                    dto.setUriKey(dto.getUriKey().replace(" ", ""));
                    return Tuples.of(category, Optional.ofNullable(dto.getParentCategoryId()));
                })
                .flatMap(tuple2 -> {
                    if (tuple2.getT1().getDepth() > 0 && tuple2.getT2().isEmpty()) {
                        return Mono.error(new RuntimeException("No Parent Category ID"));
                    } else {
                        if(tuple2.getT1().getDepth()==0){
                            return categoryRepository.save(tuple2.getT1());
                        }else{
                            return categoryRepository.findById(tuple2.getT2().get()).flatMap(parentCategory -> {
//                                tuple2.getT1().setParentCategory(parentCategory);
                                return categoryRepository.save(tuple2.getT1())
                                        .flatMap(childCategory -> {
                                    var childCategoryList = parentCategory.getChildCategoryList();
                                    if (childCategoryList == null) {
                                        childCategoryList = new ArrayList<>();
                                    }
                                    childCategoryList.add(childCategory);
                                    parentCategory.setChildCategoryList(childCategoryList);
                                    return categoryRepository.save(parentCategory);
                                })
                                        ;
                            });
                        }
                    }
                });
    }
}
