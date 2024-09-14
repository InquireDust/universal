package org.wenchen.demo.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.wenchen.demo.domain.User;
import org.wenchen.demo.domain.dto.UserDTO;


@Mapper
public interface UserConvert {
    UserConvert CONVERT = Mappers.getMapper(UserConvert.class);

    //User convert(OutParam in);
    UserDTO convert(User in);

}