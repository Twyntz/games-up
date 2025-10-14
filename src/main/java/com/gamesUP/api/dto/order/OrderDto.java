package com.gamesup.api.dto.order;

import com.gamesup.api.enumeration.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrderDto(Long id, LocalDate date, BigDecimal totalPrice, OrderStatus status, String shippingAddress, List<OrderLineDto> lines) {}