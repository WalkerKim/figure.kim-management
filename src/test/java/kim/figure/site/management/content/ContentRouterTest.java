package kim.figure.site.management.content;

import kim.figure.site.management.common.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

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

//    @BeforeEach
//    void setUp() {
//        contentRepository.findAll().subscribe(i -> System.out.println(i));
//    }

    @Test
    void contentRoutes() {
        this.webTestClient.post().uri("/content")
                .exchange()
                .expectStatus().isOk()
                .expectBody();


    }
}