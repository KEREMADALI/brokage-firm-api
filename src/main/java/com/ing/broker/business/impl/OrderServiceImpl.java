package com.ing.broker.business.impl;

import com.ing.broker.business.OrderService;
import com.ing.broker.domain.model.Asset;
import com.ing.broker.domain.model.Order;
import com.ing.broker.domain.model.State;
import com.ing.broker.domain.repo.AssetRepo;
import com.ing.broker.domain.repo.OrderRepo;
import com.ing.broker.dto.OrderDto;
import com.ing.broker.exception.NotEnoughMoneyException;
import com.ing.broker.exception.OrderAlreadyProcessedException;
import com.ing.broker.exception.OrderDoesNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

  private final OrderRepo orderRepo;
  private final AssetRepo assetRepo;

  private static final String TRY_ASSET_NAME = "TRY";

  @Override
  public UUID createOrder(UUID customerId, OrderDto orderDto) {

    Optional<Asset> tryAssetOpt = assetRepo.findAllByCustomerIdAndName(customerId, TRY_ASSET_NAME);

    if (tryAssetOpt.isPresent()
        && tryAssetOpt.get().getUsableSize() < orderDto.getTotalPrice()) {
      new NotEnoughMoneyException(customerId);
    }

    Order order = Order.createFrom(customerId, orderDto);
    return orderRepo.save(order).getId();
  }

  @Override
  public List<Order> getOrders(UUID customerId, LocalDateTime startDate, LocalDateTime endDate) {
    return orderRepo.findAllByCustomerId(customerId, startDate, endDate);
  }

  @Override
  public void deleteOrder(UUID customerId, UUID orderId) {
    Optional<Order> orderToBeDeletedOpt = orderRepo.findByIdAndCustomerId(orderId, customerId);

    if(orderToBeDeletedOpt.isPresent() == false){
      throw new OrderDoesNotExistException(customerId, orderId);
    }

    if (orderToBeDeletedOpt.get().getState() != State.PENDING) {
      throw new OrderAlreadyProcessedException(customerId, orderId);
    }

    orderRepo.deleteByIdAndCustomerId(orderId, customerId);
  }

  @Scheduled(fixedDelayString = "${order_consuming_delay}")
  private void consumeOrders() {
    // TODO check pending orders and consume
    // consume 100 and move on
  }
}
