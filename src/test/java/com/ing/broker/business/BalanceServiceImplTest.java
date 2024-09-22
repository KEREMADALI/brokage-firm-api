package com.ing.broker.business;


import com.ing.broker.business.impl.BalanceServiceImpl;
import com.ing.broker.domain.constant.AssetType;
import com.ing.broker.domain.model.Asset;
import com.ing.broker.domain.repo.AssetRepo;
import com.ing.broker.exception.NotEnoughAssetException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceImplTest {

  @Mock
  private AssetRepo assetRepo;

  @InjectMocks
  private BalanceServiceImpl balanceService;

  @Test
  public void depositMoney_succeeds() {
    // Given
    UUID customerId = UUID.randomUUID();
    int depositAmount = 1;

    // When
    balanceService.deposit(customerId, depositAmount);

    // Then
    ArgumentCaptor<Asset> assetArgumentCaptor = ArgumentCaptor.forClass(Asset.class);
    verify(assetRepo, times(1)).save(assetArgumentCaptor.capture());
    assertThat(assetArgumentCaptor.getValue().getSize()).isEqualTo(depositAmount);
    assertThat(assetArgumentCaptor.getValue().getUsableSize()).isEqualTo(depositAmount);
    assertThat(assetArgumentCaptor.getValue().getCustomerId()).isEqualTo(customerId);
    assertThat(assetArgumentCaptor.getValue().getName()).isEqualTo(AssetType.TRY.name());
  }

  @Test
  public void depositMoney_with_existing_asset_succeeds() {
    // Given
    UUID customerId = UUID.randomUUID();
    int depositAmount = 1;
    Asset existingAsset = Asset.newBuilder()
        .setCustomerId(customerId)
        .setName(AssetType.TRY.name())
        .setSize(depositAmount)
        .setUsableSize(depositAmount)
        .build();

    when(assetRepo.findByCustomerIdAndName(customerId, AssetType.TRY.name())).thenReturn(Optional.ofNullable(existingAsset));

    // When
    balanceService.deposit(customerId, depositAmount);

    // Then
    ArgumentCaptor<Asset> assetArgumentCaptor = ArgumentCaptor.forClass(Asset.class);
    verify(assetRepo, times(1)).save(assetArgumentCaptor.capture());
    assertThat(assetArgumentCaptor.getValue().getSize()).isEqualTo(depositAmount * 2);
    assertThat(assetArgumentCaptor.getValue().getUsableSize()).isEqualTo(depositAmount * 2);
  }

  @Test
  public void withdraw_succeeds() {
    // Given
    UUID customerId = UUID.randomUUID();
    int withdrawAmount = 1;
    Asset existingAsset = Asset.newBuilder()
        .setCustomerId(customerId)
        .setName(AssetType.TRY.name())
        .setSize(withdrawAmount)
        .setUsableSize(withdrawAmount)
        .build();

    when(assetRepo.findByCustomerIdAndName(customerId, AssetType.TRY.name())).thenReturn(Optional.ofNullable(existingAsset));

    // When
    balanceService.withdraw(customerId, withdrawAmount);

    // Then
    ArgumentCaptor<Asset> assetArgumentCaptor = ArgumentCaptor.forClass(Asset.class);
    verify(assetRepo, times(1)).save(assetArgumentCaptor.capture());
    assertThat(assetArgumentCaptor.getValue().getSize()).isEqualTo(0);
    assertThat(assetArgumentCaptor.getValue().getUsableSize()).isEqualTo(0);
  }

  @Test
  public void withdraw_without_enough_asset_throws_exception() {
    // Given
    UUID customerId = UUID.randomUUID();
    int withdrawAmount = 2;
    Asset existingAsset = Asset.newBuilder()
        .setCustomerId(customerId)
        .setName(AssetType.TRY.name())
        .setSize(1)
        .setUsableSize(1)
        .build();

    when(assetRepo.findByCustomerIdAndName(customerId, AssetType.TRY.name())).thenReturn(Optional.ofNullable(existingAsset));

    // When, Then
    assertThatExceptionOfType(NotEnoughAssetException.class).isThrownBy(
        () -> balanceService.withdraw(customerId, withdrawAmount));
  }

  @Test
  public void withdraw_without_existing_asset_throws_exception() {
    // Given
    UUID customerId = UUID.randomUUID();
    int withdrawAmount = 1;

    // When, Then
    assertThatExceptionOfType(NotEnoughAssetException.class).isThrownBy(
        () -> balanceService.withdraw(customerId, withdrawAmount));
  }
}
