package com.example.demo.repository.specification.user;

import com.example.demo.enums.Role;
import com.example.demo.enums.Sex;
import com.example.demo.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> hasEmailLike(String email) {
        return (root, query, cb) ->
                email == null || email.isBlank()
                        ? cb.conjunction()
                        : cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<User> hasNicknameLike(String nickname) {
        return (root, query, cb) ->
                nickname == null || nickname.isBlank()
                        ? cb.conjunction()
                        : cb.like(cb.lower(root.get("nickname")), "%" + nickname.toLowerCase() + "%");
    }

    public static Specification<User> hasPhoneLike(String phone) {
        return (root, query, cb) ->
                phone == null || phone.isBlank()
                        ? cb.conjunction()
                        : cb.like(root.get("phone"), "%" + phone + "%");
    }

    public static Specification<User> hasNameLike(String input) {
        return (root, query, cb) -> {
            if (input == null || input.isBlank()) {
                return cb.conjunction();
            }

            String[] parts = input.trim().toLowerCase().split("\\s+");

            if (parts.length == 1) {
                String value = "%" + parts[0] + "%";
                return cb.or(
                        cb.like(cb.lower(root.get("firstname")), value),
                        cb.like(cb.lower(root.get("lastname")), value)
                );
            }

            String first = "%" + parts[0] + "%";
            String second = "%" + parts[1] + "%";

            return cb.or(
                    cb.and(
                            cb.like(cb.lower(root.get("firstname")), first),
                            cb.like(cb.lower(root.get("lastname")), second)
                    ),
                    cb.and(
                            cb.like(cb.lower(root.get("firstname")), second),
                            cb.like(cb.lower(root.get("lastname")), first)
                    )
            );
        };
    }

    public static Specification<User> hasRole(Role role) {
        return (root, query, cb) ->
                role == null ? cb.conjunction()
                        : cb.equal(root.get("role"), role);
    }

    public static Specification<User> hasSex(Sex sex) {
        return (root, query, cb) ->
                sex == null ? cb.conjunction()
                        : cb.equal(root.get("sex"), sex);
    }

    public static Specification<User> isActive(Boolean active) {
        return (root, query, cb) ->
                active == null ? cb.conjunction()
                        : cb.equal(root.get("isActive"), active);
    }

    public static Specification<User> orderByLastname() {
        return (root, query, cb) -> {
            assert query != null;
            query.orderBy(cb.asc(root.get("lastname")));
            return cb.conjunction();
        };
    }
}