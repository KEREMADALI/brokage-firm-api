package com.ing.broker.business;

import com.ing.broker.domain.model.Order;
import com.ing.broker.dto.OrderDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface OrderService {

  UUID createOrder(UUID customerId, OrderDto orderDto);

  List<Order> getOrders(UUID customerId, LocalDateTime startDate, LocalDateTime endDate);

  void deleteOrder(UUID customerId, UUID orderId);
}
