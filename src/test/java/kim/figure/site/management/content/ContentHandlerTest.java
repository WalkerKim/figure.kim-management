package kim.figure.site.management.content;

import kim.figure.site.common.content.Content;
import kim.figure.site.management.common.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.config.WebFluxConfigurationSupport;
import reactor.core.publisher.Flux;

import java.time.Instant;

/**
 * author         : walker
 * date           : 2022. 12. 13.
 * description    :
 */
@SpringBootTest(classes = {ContentHandler.class, ContentService.class, ContentRouter.class, ValidationUtil.class, WebFluxConfigurationSupport.class})
class ContentHandlerTest {
    WebTestClient webTestClient;

    @MockBean
    ContentRepository contentRepository;

    @MockBean
    ContentSequenceService contentSequenceService;
    @Autowired
    ContentHandler contentHandler;
    @Autowired
    ContentRouter contentRouter;





    @BeforeEach
    public void beforeTest() {
        webTestClient = WebTestClient
                .bindToRouterFunction(contentRouter.contentRoutes(contentHandler))
                .build();
    }

    @Test
    public void handlerTest() {
//        BDDMockito.when(validator.validate())
        BDDMockito.when(contentRepository.findBy(PageRequest.of(0, 10, Sort.by("id").descending()))).thenReturn(Flux.just(Content.builder().id(1L).createdAt(Instant.now()).build()));
        webTestClient.get()
                .uri("/content?limit=10&offset=0")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(value -> {
                    System.out.println(value);
                });
    }
}