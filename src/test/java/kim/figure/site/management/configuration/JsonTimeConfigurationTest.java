package kim.figure.site.management.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * author         : walker
 * date           : 2022. 12. 13.
 * description    :
 */
@SpringBootTest
class JsonTimeConfigurationTest {
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void objectMapperModuleTest(){
        Instant instant = Instant.ofEpochMilli(0);
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("time", instant);
        System.out.println(instant);
        try {
            var test = objectMapper.writeValueAsString(testMap);
            System.out.println(test);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}