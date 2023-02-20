package kim.figure.site.management.configuration.security;

import kim.figure.site.management.account.AdminAccount;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Log4j2
@Getter
@Setter
public class CustomUserDetails extends User {
    Long type;

	private String id;

    private boolean superBool;

	private boolean tempPasswordBool;

	private int attemptCount;

//    List<ApplicationDocument> appList;

//    public CustomUserDetails() {
//
//        super("Administrator", "passw0rd", Arrays.asList(new SimpleGrantedAuthority("ADMIN"),new SimpleGrantedAuthority("ACTUATOR")));
//
//    }
    public CustomUserDetails(AdminAccount accountDocument) {
    	super(accountDocument.getUsername(),
				accountDocument.getPassword(),
				Optional.ofNullable(accountDocument.getAuthorities()).orElseGet(()->new ArrayList<>()).stream().map(i->new SimpleGrantedAuthority(i)).collect(toList()));
		tempPasswordBool = accountDocument.isTempPasswordBool();
		attemptCount = accountDocument.getAttemptCount();
		id = accountDocument.getId();

	}

//	private static List<SimpleGrantedAuthority> makeRole(Long type) {
//		List<SimpleGrantedAuthority> ret = new ArrayList<SimpleGrantedAuthority>();
//		ret.add(new SimpleGrantedAuthority("ROLE_ACTUATOR"));
//		if(type.equals(1L)) {
//			ret.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//		} else {
//			ret.add(new SimpleGrantedAuthority("ROLE_USER"));
//		}
//
//		return ret;
//	}

}