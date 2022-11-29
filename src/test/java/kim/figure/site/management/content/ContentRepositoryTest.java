package kim.figure.site.management.content;

import kim.figure.site.management.ManagementApplication;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * author         : walker
 * date           : 2022. 11. 22.
 * description    :
 */
@DataR2dbcTest
@ContextConfiguration(classes = {ManagementApplication.class})
class ContentRepositoryTest {


}