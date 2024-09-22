package com.ing.broker.business;

import com.ing.broker.business.impl.OrderServiceImpl;
import com.ing.broker.domain.constant.AssetType;
import com.ing.broker.domain.model.Asset;
import com.ing.broker.domain.model.Order;
import com.ing.broker.domain.model.Side;
import com.ing.broker.domain.model.State;
import com.ing.broker.domain.repo.AssetRepo;
import com.ing.broker.domain.repo.OrderRepo;
import com.ing.broker.dto.OrderDto;
import com.ing.broker.exception.NotEnoughAssetException;
import com.ing.broker.exception.OrderAlreadyProcessedException;
import com.ing.broker.exception.OrderDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

  @Mock
  private OrderRepo orderRepo;
  @Mock
  private AssetRepo assetRepo;
  @InjectMocks
  private OrderServiceImpl orderService;

  @Test
  public void createOrder_BUY_succeeds() {
    // Given
    UUID customerId = UUID.randomUUID();
    int tryAmount = 1;
    OrderDto orderDto = OrderDto.newBuilder()
        .setAssetName(AssetType.XAU.name())
        .setSize(1)
        .setPrice(1)
        .setSide(Side.BUY)
        .build();
    UUID expectedOrderId = UUID.randomUUID();
    Order expectedOrder = Order.newBuilder().setId(expectedOrderId).build();
    Asset existingTryAsset = Asset.newBuilder().setSize(tryAmount).setUsableSize(tryAmount).build();

    when(orderRepo.save(any())).thenReturn(expectedOrder);
    when(assetRepo.findByCustomerIdAndName(customerId, AssetType.TRY.name())).thenReturn(Optional.of(existingTryAsset));

    // When
    UUID actualOrderId = orderService.createOrder(customerId, orderDto);

    // Then
    assertThat(actualOrderId).isEqualTo(expectedOrderId);

    ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
    verify(orderRepo, times(1)).save(orderArgumentCaptor.capture());
    assertThat(orderArgumentCaptor.getValue().getSize()).isEqualTo(orderDto.getSize());
    assertThat(orderArgumentCaptor.getValue().getPrice()).isEqualTo(orderDto.getPrice());
    assertThat(orderArgumentCaptor.getValue().getAssetName()).isEqualTo(orderDto.getAssetName());
    assertThat(orderArgumentCaptor.getValue().getCustomerId()).isEqualTo(customerId);
    assertThat(orderArgumentCaptor.getValue().getSide()).isEqualTo(orderDto.getSide());
    assertThat(orderArgumentCaptor.getValue().getState()).isEqualTo(State.PENDING);

    ArgumentCaptor<Asset> assetArgumentCaptor = ArgumentCaptor.forClass(Asset.class);
    verify(assetRepo, times(1)).save(assetArgumentCaptor.capture());
    assertThat(assetArgumentCaptor.getValue().getSize()).isEqualTo(existingTryAsset.getSize());
    assertThat(assetArgumentCaptor.getValue().getUsableSize()).isEqualTo(
        tryAmount - orderDto.getTotalValue());
  }

  @Test
  public void createOrder_BUY_without_TRY_asset_throws_exception() {
    // Given
    UUID customerId = UUID.randomUUID();
    OrderDto orderDto = OrderDto.newBuilder()
        .setAssetName(AssetType.XAU.name())
        .setSize(1)
        .setPrice(1)
        .setSide(Side.BUY)
        .build();

    when(assetRepo.findByCustomerIdAndName(customerId, AssetType.TRY.name())).thenReturn(Optional.empty());

    // When, Then
    assertThatExceptionOfType(NotEnoughAssetException.class).isThrownBy(() -> orderService.createOrder(customerId, orderDto));
  }

  @Test
  public void createOrder_BUY_without_enough_TRY_asset_throws_exception() {
    // Given
    UUID customerId = UUID.randomUUID();
    OrderDto orderDto = OrderDto.newBuilder()
        .setAssetName(AssetType.XAU.name())
        .setSize(2)
        .setPrice(2)
        .setSide(Side.BUY)
        .build();
    Asset existingTryAsset = Asset.newBuilder().setUsableSize(1).build();

    when(assetRepo.findByCustomerIdAndName(customerId, AssetType.TRY.name())).thenReturn(Optional.of(existingTryAsset));

    // When, Then
    assertThatExceptionOfType(NotEnoughAssetException.class).isThrownBy(() -> orderService.createOrder(customerId, orderDto));
  }

  @Test
  public void createOrder_SELL_succeeds() {
    // Given
    UUID customerId = UUID.randomUUID();
    int tryAmount = 1;
    OrderDto orderDto = OrderDto.newBuilder()
        .setAssetName(AssetType.XAU.name())
        .setSize(1)
        .setPrice(1)
        .setSide(Side.SELL)
        .build();
    UUID expectedOrderId = UUID.randomUUID();
    Order expectedOrder = Order.newBuilder().setId(expectedOrderId).build();
    Asset existingTryAsset = Asset.newBuilder().setSize(tryAmount).setUsableSize(tryAmount).build();

    when(orderRepo.save(any())).thenReturn(expectedOrder);
    when(assetRepo.findByCustomerIdAndName(customerId, AssetType.XAU.name())).thenReturn(Optional.of(existingTryAsset));

    // When
    UUID actualOrderId = orderService.createOrder(customerId, orderDto);

    // Then
    assertThat(actualOrderId).isEqualTo(expectedOrderId);

    ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
    verify(orderRepo, times(1)).save(orderArgumentCaptor.capture());
    assertThat(orderArgumentCaptor.getValue().getSize()).isEqualTo(orderDto.getSize());
    assertThat(orderArgumentCaptor.getValue().getPrice()).isEqualTo(orderDto.getPrice());
    assertThat(orderArgumentCaptor.getValue().getAssetName()).isEqualTo(orderDto.getAssetName());
    assertThat(orderArgumentCaptor.getValue().getCustomerId()).isEqualTo(customerId);
    assertThat(orderArgumentCaptor.getValue().getSide()).isEqualTo(orderDto.getSide());
    assertThat(orderArgumentCaptor.getValue().getState()).isEqualTo(State.PENDING);

    ArgumentCaptor<Asset> assetArgumentCaptor = ArgumentCaptor.forClass(Asset.class);
    verify(assetRepo, times(1)).save(assetArgumentCaptor.capture());
    assertThat(assetArgumentCaptor.getValue().getSize()).isEqualTo(existingTryAsset.getSize());
    assertThat(assetArgumentCaptor.getValue().getUsableSize()).isEqualTo(
        tryAmount - orderDto.getTotalValue());
  }

  @Test
  public void createOrder_SELL_without_asset_throws_exception() {
    // Given
    UUID customerId = UUID.randomUUID();
    OrderDto orderDto = OrderDto.newBuilder()
        .setAssetName(AssetType.XAU.name())
        .setSize(1)
        .setPrice(1)
        .setSide(Side.SELL)
        .build();

    when(assetRepo.findByCustomerIdAndName(customerId, AssetType.XAU.name())).thenReturn(Optional.empty());

    // When, Then
    assertThatExceptionOfType(NotEnoughAssetException.class).isThrownBy(() -> orderService.createOrder(customerId, orderDto));
  }

  @Test
  public void createOrder_SELL_without_enough_asset_throws_exception() {
    // Given
    UUID customerId = UUID.randomUUID();
    OrderDto orderDto = OrderDto.newBuilder()
        .setAssetName(AssetType.XAU.name())
        .setSize(2)
        .setPrice(2)
        .setSide(Side.SELL)
        .build();
    Asset existingTryAsset = Asset.newBuilder().setUsableSize(1).build();

    when(assetRepo.findByCustomerIdAndName(customerId, AssetType.XAU.name())).thenReturn(Optional.of(existingTryAsset));

    // When, Then
    assertThatExceptionOfType(NotEnoughAssetException.class).isThrownBy(() -> orderService.createOrder(customerId, orderDto));
  }

  @Test
  public void getOrders_succeeds() {
    // Given
    UUID customerId = UUID.randomUUID();
    LocalDateTime localDateTime = LocalDateTime.now();

    // When
    orderService.getOrders(customerId, localDateTime, localDateTime);

    // Then
    verify(orderRepo, times(1)).findAllByCustomerId(customerId, localDateTime, localDateTime);
  }

  @Test
  public void deleteOrder_succeeds() {
    // Given
    UUID customerId = UUID.randomUUID();
    UUID orderId = UUID.randomUUID();
    Order existingOrder = Order.newBuilder().setState(State.PENDING).build();
    Asset existingTryAsset = Asset.newBuilder().build();

    when(orderRepo.findByIdAndCustomerId(orderId, customerId)).thenReturn(Optional.of(existingOrder));
    when(assetRepo.findByCustomerIdAndName(customerId, AssetType.TRY.name())).thenReturn(Optional.of(existingTryAsset));

    // When
    orderService.deleteOrder(customerId, orderId);

    // Then
    ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
    verify(orderRepo, times(1)).save(orderArgumentCaptor.capture());
    assertThat(orderArgumentCaptor.getValue().getState()).isEqualTo(State.CANCELED);
  }

  @Test
  public void deleteOrder_without_order_throws_error() {
    // Given
    UUID customerId = UUID.randomUUID();
    UUID orderId = UUID.randomUUID();

    when(orderRepo.findByIdAndCustomerId(orderId, customerId)).thenReturn(Optional.empty());

    // When, Then
    assertThatExceptionOfType(OrderDoesNotExistException.class).isThrownBy(() -> orderService.deleteOrder(customerId, orderId));
  }

  @ParameterizedTest
  @EnumSource(value = State.class, names = {"CANCELED","MATCHED"})
  public void deleteOrder_with_processed_order_throws_error(State state) {
    // Given
    UUID customerId = UUID.randomUUID();
    UUID orderId = UUID.randomUUID();
    Order existingOrder = Order.newBuilder().setState(state).build();

    when(orderRepo.findByIdAndCustomerId(orderId, customerId)).thenReturn(Optional.of(existingOrder));

    // When, Then
    assertThatExceptionOfType(OrderAlreadyProcessedException.class).isThrownBy(() -> orderService.deleteOrder(customerId, orderId));
  }
}
