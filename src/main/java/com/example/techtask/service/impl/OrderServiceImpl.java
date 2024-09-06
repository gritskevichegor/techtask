package com.example.techtask.service.impl;

import com.example.techtask.model.Order;
import com.example.techtask.service.OrderService;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final EntityManager entityManager;

    public OrderServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Order findOrder() {
        String query = "select * from orders " +
                "where quantity > 1 " +
                "order by created_at desc limit 1";
        return (Order) entityManager.createNativeQuery(query, Order.class)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Order> findOrders() {
        String query = "select o.* from orders o " +
                "left join users u on u.id = o.user_id " +
                "where u.\"user_status\" = 'ACTIVE' " +
                "order by o.created_at";
        return entityManager.createNativeQuery(query, Order.class)
                .getResultList();
    }
}
