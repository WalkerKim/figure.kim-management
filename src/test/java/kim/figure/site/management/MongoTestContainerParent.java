package kim.figure.site.management;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;

/**
 * author         : walker
 * date           : 2023. 03. 07.
 * description    :
 */
//@SpringBootTest
@Testcontainers
@Disabled
public class MongoTestContainerParent {


    private static String DATABASE = "figure";
    private static String USERNAME = "figure";
    private static String PASSWORD = "mongo-password";

    private static String ROOT_USERNAME = "root";
    private static String ROOT_PASSWORD = "example";

    public static final long memoryInBytes = 64 * 1024 * 1024;

    @Container
    public static final MongoDBContainer MONGO_DB_CONTAINER;

    static{
        MONGO_DB_CONTAINER = new MongoDBContainer("mongo:4.4.6");
//        MONGO_DB_CONTAINER
//                .setEnv(List.of(
//                        "MONGO_INITDB_ROOT_USERNAME=" + ROOT_USERNAME,
//                        "MONGO_INITDB_ROOT_PASSWORD=" + ROOT_PASSWORD
////                        ,
////                        "MONGO_INITDB_DATABASE=" + DATABASE
//                ));
        MONGO_DB_CONTAINER.start();
//        MONGO_DB_CONTAINER.waitingFor(Wait.forLogMessage("(?i).*Waiting for connections*.*", 1));
//        MONGO_DB_CONTAINER.waitingFor(Wait.forListeningPort());
        MONGO_DB_CONTAINER.copyFileToContainer(MountableFile.forClasspathResource("entrypoint.js"), "/script/init.js");
        try {
            MONGO_DB_CONTAINER.execInContainer("mongo", "/script/init.js");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        System.out.println("MONGO_DB_CONTAINER.getMappedPort(27017) = " + MONGO_DB_CONTAINER.getMappedPort(27017));
        registry.add("spring.data.mongodb.port=", () -> MONGO_DB_CONTAINER.getMappedPort(27017));
        registry.add("spring.data.mongodb.username=", () -> "root");
//        registry.add("spring.data.mongodb.password=", () -> PASSWORD);
        registry.add("spring.data.mongodb.database=", () -> DATABASE);

    }


    @BeforeAll
    static void beforeAll() {

    }

    @AfterAll
    static void finish() {

    }

}
