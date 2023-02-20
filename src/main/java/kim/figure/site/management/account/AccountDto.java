package kim.figure.site.management.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AccountDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Get{
        private String id;
        private String username;
        private Collection<GrantedAuthority> authorities;
        private boolean superBool;
        private boolean accountNonExpired;
        private boolean accountNonLocked;
        private boolean credentialsNonExpired;
        private boolean enabled;
        private boolean tempPasswordBool;
        private String locale;
    }


    @Data
    public static class Post{
        @Email
        @NotBlank
        private String username;
        @NotBlank
        private String password;
        @NotBlank
        private String passwordConfirm;
        @NotBlank
        private String name;
    }

    @Data
    public static class Put {
        private String password;
        private String name;

    }

    @Data
    public static class ChangePassword {
        @NotBlank
        private String currentPassword;
        @NotBlank
        private String password;
        @NotBlank
        private String passwordConfirm;
    }
}
