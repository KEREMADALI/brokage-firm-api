package com.ing.broker.business.impl;

import com.ing.broker.business.OrderService;
import com.ing.broker.domain.model.Asset;
import com.ing.broker.domain.constant.AssetType;
import com.ing.broker.domain.model.Order;
import com.ing.broker.domain.model.Side;
import com.ing.broker.domain.model.State;
import com.ing.broker.domain.repo.AssetRepo;
import com.ing.broker.domain.repo.OrderRepo;
import com.ing.broker.dto.OrderDto;
import com.ing.broker.exception.NotEnoughAssetException;
import com.ing.broker.exception.OrderAlreadyProcessedException;
import com.ing.broker.exception.OrderDoesNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  @Override
  public UUID createOrder(UUID customerId, OrderDto orderDto) {
    if(orderDto.getSide() == Side.BUY){
      verifyAssetAmount(customerId, AssetType.TRY.name(), orderDto.getTotalValue());
    }
    else if (orderDto.getSide() == Side.SELL){
      verifyAssetAmount(customerId, orderDto.getAssetName(), orderDto.getSize());
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

    if (orderToBeDeletedOpt.isPresent() == false) {
      throw new OrderDoesNotExistException(customerId, orderId);
    }

    if (orderToBeDeletedOpt.get().getState() != State.PENDING) {
      throw new OrderAlreadyProcessedException(customerId, orderId);
    }

    Asset tryAsset = assetRepo.findByCustomerIdAndName(customerId, AssetType.TRY.name()).get();
    tryAsset.setUsableSize(tryAsset.getUsableSize() + orderToBeDeletedOpt.get().getSize());
    assetRepo.save(tryAsset);

    Order newOrder = orderToBeDeletedOpt.get();
    newOrder.setState(State.CANCELED);
    orderRepo.save(newOrder);
  }

  private void verifyAssetAmount(UUID customerId, String assetName, int amount){
    Optional<Asset> assetOpt = assetRepo.findByCustomerIdAndName(customerId, assetName);

    if(assetOpt.isEmpty()
        || assetOpt.get().getUsableSize() < amount){
      throw new NotEnoughAssetException(customerId,assetName);
    }

    Asset newAsset = assetOpt.get();
    newAsset.setUsableSize(newAsset.getUsableSize() - amount);
    assetRepo.save(newAsset);
    log.info("Assets are enough.");
  }
}
