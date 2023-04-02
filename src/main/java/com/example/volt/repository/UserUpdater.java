package com.example.volt.repository;

import com.example.volt.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserUpdater {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void updateUser(User user) {
        entityManager.merge(user);
    }
}
