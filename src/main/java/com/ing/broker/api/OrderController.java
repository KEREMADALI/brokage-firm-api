package com.ing.broker.api;

import com.ing.broker.business.OrderService;
import com.ing.broker.domain.model.Order;
import com.ing.broker.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@RestController
@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public UUID createOrder(@RequestParam("customerId") UUID customerId, @RequestBody @NotNull OrderDto orderDto) {
    log.info("Got request for createOrder(). CustomerId: %s", customerId);

    return orderService.createOrder(customerId, orderDto);
  }

  @GetMapping
  public List<Order> listOrder(@RequestParam("customerId") UUID customerId,
      @RequestParam(name = "startDate") @DateTimeFormat(iso = DATE_TIME) LocalDateTime startDate,
      @RequestParam(name = "endDate") @DateTimeFormat(iso = DATE_TIME) LocalDateTime endDate) {
    log.info("Got request for listOrder(). CustomerId: %s", customerId);

    return orderService.getOrders(customerId, startDate, endDate);
  }

  @DeleteMapping(value = "/{orderId}")
  public void deleteOrder(@RequestParam("customerId") UUID customerId, @PathVariable("orderId") UUID orderId) {
    log.info("Got request for deleteOrder(). CustomerId: %s", customerId);

    orderService.deleteOrder(customerId, orderId);
  }
}
