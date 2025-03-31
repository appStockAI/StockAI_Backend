package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<User> findByUsernameOrEmail(String input) {
        String jpql = "SELECT u FROM User u WHERE u.username =:input OR u.email =:input";

        return em.createQuery(jpql, User.class)
                .setParameter("input", input)
                .getResultStream()
                .findFirst();
    }
}
