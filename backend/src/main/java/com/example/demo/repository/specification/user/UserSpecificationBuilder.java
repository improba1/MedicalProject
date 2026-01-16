package com.example.demo.repository.specification.user;

import com.example.demo.dto.request.user.UserSearchRequest;
import com.example.demo.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecificationBuilder {

    public static Specification<User> build(UserSearchRequest r) {
        return Specification.allOf(
                UserSpecifications.hasEmailLike(r.getEmail()),
                UserSpecifications.hasNicknameLike(r.getNickname()),
                UserSpecifications.hasPhoneLike(r.getPhone()),
                UserSpecifications.hasNameLike(r.getName()),
                UserSpecifications.hasRole(r.getRole()),
                UserSpecifications.hasSex(r.getSex()),
                UserSpecifications.isActive(r.getIsActive()),
                UserSpecifications.orderByLastname()
        );
    }
}