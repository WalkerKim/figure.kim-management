package kim.figure.site.management.content;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * author         : walker
 * date           : 2022. 11. 30.
 * description    :
 */
@Service
public class ContentSequenceService {
    final MongoOperations mongoOperations;

    public ContentSequenceService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public long getSeq(String sequenceName){
        ContentSequence sequencer = mongoOperations.findAndModify(query(where("_id").is(sequenceName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                ContentSequence.class);
        return !Objects.isNull(sequencer) ? sequencer.getSeq() : 1;
    }
}
