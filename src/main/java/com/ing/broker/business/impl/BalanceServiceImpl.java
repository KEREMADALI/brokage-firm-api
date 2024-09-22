package com.ing.broker.business.impl;

import com.ing.broker.business.BalanceService;
import com.ing.broker.domain.model.Asset;
import com.ing.broker.domain.constant.AssetType;
import com.ing.broker.domain.repo.AssetRepo;
import com.ing.broker.exception.NotEnoughAssetException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceServiceImpl implements BalanceService {

  private final AssetRepo assetRepo;

  @Override
  public void deposit(UUID customerId, int amount) {
    Optional<Asset> tryAssetOpt = assetRepo.findByCustomerIdAndName(customerId, AssetType.TRY.name());

    Asset newTryAsset = tryAssetOpt.map(asset -> {
          asset.setSize(asset.getSize() + amount);
          asset.setUsableSize(asset.getUsableSize() + amount);
          return asset;
        })
        .orElse(Asset.newBuilder()
            .setCustomerId(customerId)
            .setSize(amount)
            .setUsableSize(amount)
            .setName(AssetType.TRY.name())
            .build());

    assetRepo.save(newTryAsset);
  }

  @Override
  public void withdraw(UUID customerId, int amount) {
    Optional<Asset> tryAssetOpt = assetRepo.findByCustomerIdAndName(customerId, AssetType.TRY.name());

    Asset newTryAsset = tryAssetOpt.map(asset -> {
          if(asset.getUsableSize() < amount){
            throw new NotEnoughAssetException(customerId, AssetType.TRY.name());
          }

          asset.setSize(asset.getSize() - amount);
          asset.setUsableSize(asset.getUsableSize() - amount);
          return asset;
        })
        .orElseThrow(() -> new NotEnoughAssetException(customerId, AssetType.TRY.name()));

    assetRepo.save(newTryAsset);
  }
}
