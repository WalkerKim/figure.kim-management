

package kim.figure.site.management.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import kim.figure.site.common.content.Content;
import kim.figure.site.common.content.ContentFormat;
import kim.figure.site.common.tag.Tag;
import kim.figure.site.management.MongoTestContainerParent;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * author         : walker
 * date           : 2022. 11. 22.
 * description    :
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ContentRouterTest extends MongoTestContainerParent {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ReactiveMongoOperations reactiveMongoOperations;

    @Autowired
    ContentRepository contentRepository;

    /**
     * 1.
     * when click create button, client post request to /temp-content
     * given : just login
     * when : call post /temp-content
     * then : temp content will be made with timestamp id and return 200 with temp content id
     * 2.
     * 2-1.
     * given : ContentDto.Post tagList is empty
     * when : call put /content/{id}
     * then : return 200. and saved content tagList is empty
     * 2-2.
     * given : ContentDto.Post consists of tag1, tag2, tag3
     * when : call put /content/{id}
     * then : return 200. and saved content tagList consists of tag1, tag2, tag3
     */
    @BeforeAll
    static void setUp(@Autowired MongoOperations mongoOperations) {
        mongoOperations.getCollectionNames().forEach(collectionName -> mongoOperations.dropCollection(collectionName));
    }


    @Nested
    @DisplayName("content 생명주기 테스트")
    class contentLifeCycleTest{
        @Test
        @WithMockUser(authorities = "ADMIN")
        @DisplayName("/temp-content 요청시 post 생성 및 생성된 post가 반환되어야 한다.")
        void tempContentPost() {
            webTestClient.post().uri("/temp-content")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody().jsonPath("$.id").isNotEmpty()
                    .consumeWith( exchangeResult ->
                            System.out.println(new String(exchangeResult.getResponseBody()))
                    );
        }
        @Test
        @WithMockUser(authorities = "ADMIN")
        @DisplayName("temp content 작성중 /put/{id}로 임시 저장할 경우 추가된 내용이 잘 반영되어야 한다.")
        void tempContentPut() {
            tempContentPost();
            var tempContent = contentRepository.findAll().filter(i->i.getId()>10000000).take(1).single().block();
            ContentDto.Put tempContentPutDto = ContentMapper.INSTANCE.contentEntityToPut(tempContent);
            tempContentPutDto.setTagList(List.of(new Tag("tag1"), new Tag("tag2"), new Tag("tag3")));
            tempContentPutDto.setCategoryIdList(List.of());
            tempContentPutDto.setRenderedContent("<h1>renderedContent</h1>");
            tempContentPutDto.setRawContent("# rawContent");
            tempContentPutDto.setTitle("title test");
            tempContentPutDto.setContentFormat(ContentFormat.MARKDOWN);
            webTestClient.put().uri("/content/" + tempContent.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(tempContentPutDto), Content.class)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    //json path test Content.class structure
                    .jsonPath("$.id").value(id -> assertEquals(tempContentPutDto.getId(), Long.valueOf(id.toString())))
                    .jsonPath("$.title").value(title -> assertEquals(tempContentPutDto.getTitle(), title))
                    .jsonPath("$.tagList").isArray()
                    .jsonPath("$.tagList").value(hasSize(tempContentPutDto.getTagList().size()))
                    .jsonPath("$.tagList[*].id").value(containsInAnyOrder(tempContentPutDto.getTagList().stream().map(Tag::getId).toArray()))
                    .jsonPath("$.renderedContent").value(renderedContent -> assertEquals(tempContentPutDto.getRenderedContent(), renderedContent))
                    .jsonPath("$.rawContent").value(rawContent -> assertEquals(tempContentPutDto.getRawContent(), rawContent));
        }

        @Test
        @WithMockUser(authorities = "ADMIN")
        @DisplayName("temp content 작성중 /content로 POST요청을 통해 저장할 경우 추가된 내용이 잘 반영되어야 한다. id는 타임스탬프 대신 신규 increment값이 할당되어야 함")
        void contentPost() {
            tempContentPost();
            var tempContent = contentRepository.findAll().take(1).single().block();
            ContentDto.Post tempContentPostDto = ContentMapper.INSTANCE.contentEntityToPost(tempContent);
            tempContentPostDto.setTagList(List.of(new Tag("tag3"), new Tag("tag4"), new Tag("tag5")));
            tempContentPostDto.setCategoryIdList(List.of());
            tempContentPostDto.setRenderedContent("<h1>renderedContent2</h1>");
            tempContentPostDto.setRawContent("# rawContent2");
            tempContentPostDto.setTitle("title test2");
            tempContentPostDto.setContentFormat(ContentFormat.MARKDOWN);
            webTestClient.post().uri("/content")
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(tempContentPostDto), Content.class)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    //json path test Content.class structure
                    .jsonPath("$.id").value(id -> assertTrue(() -> Long.parseLong(id.toString()) < 1000l))
                    .jsonPath("$.title").value(title -> assertEquals(tempContentPostDto.getTitle(), title))
                    .jsonPath("$.tagList").isArray()
                    .jsonPath("$.tagList").value(hasSize(tempContentPostDto.getTagList().size()))
                    .jsonPath("$.tagList[*].id").value(containsInAnyOrder(tempContentPostDto.getTagList().stream().map(Tag::getId).toArray()))
                    .jsonPath("$.renderedContent").value(renderedContent -> assertEquals(tempContentPostDto.getRenderedContent(), renderedContent))
                    .jsonPath("$.rawContent").value(rawContent -> assertEquals(tempContentPostDto.getRawContent(), rawContent))
                    .consumeWith(exchangeResult ->
                        {
                            //temp content should be deleted, and new content should be saved
                            StepVerifier.create(contentRepository.findAll())
                                    .expectNextMatches(content -> content.getId() == 1L)
                                    .expectComplete()
                                    .verify();
                        }
                    );
            ;


        }

        @Test
        @WithMockUser(authorities = "ADMIN")
        @DisplayName("tag List 를 비워서 /content로 POST요청을 통해 저장할 경우 추가된 내용이 잘 반영되어야 한다.")
        void withoutTagListPost(){
            tempContentPost();
            var tempContent = contentRepository.findAll().take(1).single().block();
            ContentDto.Post tempContentPostDto = ContentMapper.INSTANCE.contentEntityToPost(tempContent);
            tempContentPostDto.setTagList(List.of());
            tempContentPostDto.setCategoryIdList(List.of());
            tempContentPostDto.setRenderedContent("<h1>renderedContent2</h1>");
            tempContentPostDto.setRawContent("# rawContent2");
            tempContentPostDto.setTitle("title test2");
            tempContentPostDto.setContentFormat(ContentFormat.MARKDOWN);
            webTestClient.post().uri("/content")
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(tempContentPostDto), Content.class)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    //json path test Content.class structure
                    .jsonPath("$.id").value(id -> assertTrue(() -> Long.parseLong(id.toString()) < 1000l))
                    .jsonPath("$.title").value(title -> assertEquals(tempContentPostDto.getTitle(), title))
                    .jsonPath("$.tagList").isArray()
                    .jsonPath("$.tagList").value(hasSize(0))
                    .jsonPath("$.tagList[*].id").value(containsInAnyOrder(tempContentPostDto.getTagList().stream().map(Tag::getId).toArray()))
                    .jsonPath("$.renderedContent").value(renderedContent -> assertEquals(tempContentPostDto.getRenderedContent(), renderedContent))
                    .jsonPath("$.rawContent").value(rawContent -> assertEquals(tempContentPostDto.getRawContent(), rawContent))
                    .consumeWith(exchangeResult ->
                            {
                                //temp content should be deleted, and new content should be saved
                                StepVerifier.create(contentRepository.findAll())
                                        .expectNextMatches(content -> content.getId() == 1L)
                                        .expectComplete()
                                        .verify();
                            }
                    );
        }
        @Test
        @WithMockUser(authorities = "ADMIN")
        @DisplayName("tag List 를 비워서 /content로 PUT요청을 통해 저장할 경우 추가된 내용이 잘 반영되어야 한다.")
        void withoutTagListPut(){
            tempContentPost();
            var tempContent = contentRepository.findAll().take(1).single().block();
            ContentDto.Put tempContentPutDto = ContentMapper.INSTANCE.contentEntityToPut(tempContent);
            tempContentPutDto.setTagList(List.of());
            tempContentPutDto.setCategoryIdList(List.of());
            tempContentPutDto.setRenderedContent("<h1>renderedContent</h1>");
            tempContentPutDto.setRawContent("# rawContent");
            tempContentPutDto.setTitle("title test");
            tempContentPutDto.setContentFormat(ContentFormat.MARKDOWN);
            webTestClient.put().uri("/content/" + tempContent.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(tempContentPutDto), Content.class)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    //json path test Content.class structure
                    .jsonPath("$.id").value(id -> assertEquals(tempContentPutDto.getId(), Long.valueOf(id.toString())))
                    .jsonPath("$.title").value(title -> assertEquals(tempContentPutDto.getTitle(), title))
                    .jsonPath("$.tagList").isArray()
                    .jsonPath("$.tagList").value(hasSize(0))
                    .jsonPath("$.tagList[*].id").value(containsInAnyOrder(tempContentPutDto.getTagList().stream().map(Tag::getId).toArray()))
                    .jsonPath("$.renderedContent").value(renderedContent -> assertEquals(tempContentPutDto.getRenderedContent(), renderedContent))
                    .jsonPath("$.rawContent").value(rawContent -> assertEquals(tempContentPutDto.getRawContent(), rawContent));
        }

        @BeforeEach
        void deleteContentRepository() {
            contentRepository.deleteAll().block();
            reactiveMongoOperations.remove(ContentSequence.class).all().block();
        }
    }

    /**
     *      given : ContentDto.Put tagList is empty
     *      when : call put /content/{id}
     *      then : return 200. and saved content tagList is empty
     *
     *      */
    @Test
    @WithMockUser(authorities = "ADMIN")
    //     * 2-1.
    //     * given : ContentDto.Post tagList is empty
    //     * when : call put /content/{id}
    //     * then : return 200. and saved content tagList is empty

    void putContentRoutes() {
        var tempContent = contentRepository.findAll().single().block();
        ContentDto.Post tempContentPostDto = ContentMapper.INSTANCE.contentEntityToPost(tempContent);
        tempContentPostDto.setTagList(List.of());
        tempContentPostDto.setCategoryIdList(List.of());
        tempContentPostDto.setRenderedContent("<h1>renderedContent</h1>");
        tempContentPostDto.setRawContent("# rawContent");
        tempContentPostDto.setTitle("title test");
        tempContentPostDto.setContentFormat(ContentFormat.MARKDOWN);
        webTestClient.post().uri("/content/" + tempContent.getId())
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(tempContentPostDto), Content.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                //json path test Content.class structure
                .jsonPath("$.id").value(id -> assertTrue(() -> tempContentPostDto.getId() < 1000l))
                .jsonPath("$.title").value(title -> assertEquals(tempContentPostDto.getTitle(), title))
                .jsonPath("$.tagList").isArray()
                .jsonPath("$.tagList").value(hasSize(tempContentPostDto.getTagList().size()))
                .jsonPath("$.tagList[*].id").value(containsInAnyOrder(tempContentPostDto.getTagList().stream().map(Tag::getId).toArray()))
                .jsonPath("$.renderedContent").value(renderedContent -> assertEquals(tempContentPostDto.getRenderedContent(), renderedContent))
                .jsonPath("$.rawContent").value(rawContent -> assertEquals(tempContentPostDto.getRawContent(), rawContent));
    }

}