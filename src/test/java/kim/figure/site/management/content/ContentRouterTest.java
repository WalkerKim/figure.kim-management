package kim.figure.site.management.content;

import kim.figure.site.common.content.Content;
import kim.figure.site.management.common.ValidationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

/**
 * author         : walker
 * date           : 2022. 11. 22.
 * description    :
 */
@SpringBootTest
@ContextConfiguration(classes = {ContentRouter.class,ContentHandler.class, ContentRepository.class, ValidationUtil.class})
@AutoConfigureWebTestClient
class ContentRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    ContentRepository contentRepository;

    @Test
    void contentRoutes() {
        this.webTestClient.post().uri("/content")
                .exchange()
                .expectStatus().isOk()
                .expectBody();


    }
    @Test
    @DisplayName("content post Test")
    void postContentTest(){

        when(contentRepository.save(Content.builder().build())).thenReturn(Mono.just(Content.builder().id(1L).build()));
        this.webTestClient.post().uri("/content")
                .body(Mono.just(ContentDto.Post.builder()
                                .contentFormat("MARKDOWN")
                        .build()), ContentDto.Post.class)

                .exchange();
    }



}