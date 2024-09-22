package com.ing.broker.api;

import com.ing.broker.business.OrderService;
import com.ing.broker.domain.model.Order;
import com.ing.broker.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @PostMapping(value = "/create")
  public ResponseEntity<String> createOrder(@RequestParam("customerId") UUID customerId,
      @RequestBody @NotNull OrderDto orderDto) {
    log.info("Got request for createOrder(). CustomerId: {}", customerId);

    UUID orderId = orderService.createOrder(customerId, orderDto);

    log.info("Order created with id: {}", orderId);
    return ResponseEntity.status(HttpStatus.OK).body(String.format("Order created with id: %s", orderId));
  }

  @GetMapping(value = "list")
  public ResponseEntity<List<Order>> listOrder(@RequestParam("customerId") UUID customerId,
      @RequestParam(name = "startDate") @DateTimeFormat(iso = DATE_TIME) LocalDateTime startDate,
      @RequestParam(name = "endDate") @DateTimeFormat(iso = DATE_TIME) LocalDateTime endDate) {
    log.info("Got request for listOrder(). CustomerId: {}", customerId);

    List<Order> orders = orderService.getOrders(customerId, startDate, endDate);

    log.info("{} order(s) found.", orders.size());
    return ResponseEntity.status(HttpStatus.OK).body(orders);
  }

  @DeleteMapping(value = "/{orderId}/delete")
  public ResponseEntity<String> deleteOrder(@RequestParam("customerId") UUID customerId, @PathVariable("orderId") UUID orderId) {
    log.info("Got request for deleteOrder(). CustomerId: {}", customerId);

    orderService.deleteOrder(customerId, orderId);

    log.info("Order deleted");
    return ResponseEntity.status(HttpStatus.OK).body("Order deleted successfully.");
  }
}
