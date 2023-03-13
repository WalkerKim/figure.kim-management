package kim.figure.site.management.content;

import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
    final ReactiveMongoOperations reactiveMongoOperations;

    public ContentSequenceService(ReactiveMongoOperations reactiveMongoOperations) {
        this.reactiveMongoOperations = reactiveMongoOperations;
    }

    public Mono<Long> getSeq(String sequenceName){
        Mono<ContentSequence> sequencer = reactiveMongoOperations.findAndModify(query(where("_id").is(sequenceName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                ContentSequence.class);
        return !Objects.isNull(sequencer) ? sequencer.map(ContentSequence::getSeq) : Mono.just(1L);
    }
}
