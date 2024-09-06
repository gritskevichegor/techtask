package com.example.techtask.service.impl;

import com.example.techtask.model.User;
import com.example.techtask.service.UserService;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final EntityManager entityManager;

    public UserServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User findUser() {
        String query = "select u.id, u.email, u.\"user_status\" from users u " +
                "inner join orders o on o.user_id = u.id " +
                "where o.created_at BETWEEN '2003-01-01' AND '2003-12-31' and o.order_status = 'DELIVERED' " +
                "group by u.id " +
                "having sum(price*quantity) = (" +
                "select max(total_sum) from (" +
                "select sum(price*quantity) as total_sum from orders o " +
                "where o.created_at BETWEEN '2003-01-01' AND '2003-12-31' and o.order_status = 'DELIVERED' " +
                "group by o.user_id" +
                ") as max_result" +
                ")";
        return (User) entityManager.createNativeQuery(query, User.class)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findUsers() {
        String query = "select distinct u.id, u.email, u.\"user_status\" from users u " +
                "inner join orders o on o.user_id = u.id " +
                "where o.order_status = 'PAID' and o.created_at BETWEEN '2010-01-01' AND '2010-12-31'";
        return entityManager.createNativeQuery(query, User.class)
                .getResultList();
    }
}
