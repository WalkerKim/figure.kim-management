package kim.figure.site.management.content;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * author         : walker
 * date           : 2022. 11. 30.
 * description    :
 */
@Document("content_sequence")
public class ContentSequence {
    @Id
    private String id;

    public long getSeq() {
        return seq;
    }

    private long seq;


}
