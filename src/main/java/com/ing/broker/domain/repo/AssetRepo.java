package com.ing.broker.domain.repo;

import com.ing.broker.domain.model.Asset;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetRepo extends CrudRepository<Asset, UUID> {

  Optional<Asset> findAllByCustomerIdAndName(UUID customerId, String assetName);
}
