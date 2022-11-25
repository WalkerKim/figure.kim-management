package kim.figure.site.management.content;

import kim.figure.site.common.content.Content;
import kim.figure.site.common.content.ContentFormat;
import kim.figure.site.management.ManagementApplication;
import kim.figure.site.management.configuration.H2R2DbcConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;

/**
 * author         : walker
 * date           : 2022. 11. 22.
 * description    :
 */
@DataR2dbcTest
@ContextConfiguration(classes = {ManagementApplication.class, H2R2DbcConfig.class})
class ContentRepositoryTest {

    @Autowired
    ContentRepository contentRepository;

    @BeforeEach
    void beforeAll(){
        contentRepository.deleteAll();
    }


    @Test
    void findBy() {
        Flux<Content> testContent = Flux.range(0, 100)
                .map(i -> (ContentFormat.MARKDOWN)
                        .rawContent("test : " +i)
                        .build())
                ;

        contentRepository.saveAll(testContent).subscribe(i-> Assertions.assertNotNull(i));
        PageRequest pageble = PageRequest.of(1,5, Sort.by(Sort.Direction.DESC, "rawContent"));
        contentRepository.findBy(pageble).subscribe(i -> System.out.println(i.getRawContent()));
//        Page testPage = p
    }

}