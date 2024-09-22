package com.ing.broker.api;

import com.ing.broker.business.BalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/balance")
@Slf4j
@RequiredArgsConstructor
public class BalanceController {

  private final BalanceService balanceService;

  @PostMapping("/deposit")
  public ResponseEntity<String> depositMoney(@RequestParam("customerId") UUID customerId, @RequestBody int amount) {
    log.info("Got request for depositMoney(). CustomerId: {}", customerId);

    balanceService.deposit(customerId, amount);

    log.info("Success!");

    return ResponseEntity.status(HttpStatus.OK).body("Success.");
  }

  @PostMapping("/withdraw")
  public ResponseEntity<String> withdrawMoney(@RequestParam("customerId") UUID customerId, @RequestBody int amount) {
    log.info("Got request for withdrawMoney(). CustomerId: {}", customerId);

    balanceService.withdraw(customerId, amount);

    log.info("Success!");

    return ResponseEntity.status(HttpStatus.OK).body("Success.");
  }
}
