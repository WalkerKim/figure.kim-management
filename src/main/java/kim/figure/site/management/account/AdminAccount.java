package kim.figure.site.management.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;


@Document
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AdminAccount {


    @Id
    private String id;

    private String name;

    private String username;

    @JsonIgnore
    private String password;


    private boolean tempPasswordBool;

    @JsonIgnore
    private String tempPassword;

    private List<String> authorities;

    private boolean isActive;

    @Field
    int attemptCount;




}
