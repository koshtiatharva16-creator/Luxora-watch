package com.luxora.service;

import com.luxora.model.StoreOrder;
import com.luxora.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orders;

    public OrderService(OrderRepository orders) {
        this.orders = orders;
    }

    public StoreOrder save(StoreOrder order) {
        return orders.save(order);
    }

    public List<StoreOrder> forUser(Long id) {
        return orders.findByUserIdOrderByCreatedAtDesc(id);
    }

    public List<StoreOrder> all() {
        return orders.findAll();
    }

    public long count() {
        return orders.count();
    }
}
