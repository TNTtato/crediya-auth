package com.crediya.api.util;

import com.crediya.api.model.RegisterUserRequest;
import com.crediya.model.user.User;

public class MapperUtil {

    public static User fromRequestToUserDomain(RegisterUserRequest registerUserRequest) {
        User user = new User();
        user.setName(registerUserRequest.name());
        user.setLastName(registerUserRequest.lastName());
        user.setEmail(registerUserRequest.email());
        user.setCardId(registerUserRequest.cardId());
        user.setRoleId(registerUserRequest.roleId());
        user.setPhone(registerUserRequest.phone());
        user.setBaseSalary(registerUserRequest.baseSalary());

        return user;
    }
}
