package kim.figure.site.management.common.mapper;

import kim.figure.site.management.account.AccountDto;
import kim.figure.site.management.configuration.security.CustomUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    AccountDto.Get userDetailsToAccountDto(CustomUserDetails customUserDetails);
}