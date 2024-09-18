package com.ing.broker.domain.repo;

import com.ing.broker.domain.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepo extends CrudRepository<Order, UUID> {

  List<Order> findByCustomerIdAndCreatedAtBetween(UUID customerId,
      LocalDateTime start,
      LocalDateTime end);

  void deleteByIdAndCustomerId(UUID orderId, UUID customerId);

  Optional<Order> findByIdAndCustomerId(UUID orderId, UUID customerId);

  default List<Order> findAllByCustomerId(UUID customerId,
      LocalDateTime startDate,
      LocalDateTime endDate) {
    return findByCustomerIdAndCreatedAtBetween(customerId, startDate, endDate);
  }
}
