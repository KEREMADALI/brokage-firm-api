package com.ing.broker.business;

import java.util.UUID;

public interface BalanceService {
  void deposit(UUID customerId, int amount);

  void withdraw(UUID customerId, int amount);
}
