package kim.figure.site.management.account;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminAccountMapper {
    AdminAccountMapper INSTANCE = Mappers.getMapper(AdminAccountMapper.class);
    AdminAccount copyOfAdminAccount(AdminAccount adminAccount);

}
