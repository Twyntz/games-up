package com.gamesup.api.unit.service;

import com.gamesup.api.dto.order.CreateOrderDto;
import com.gamesup.api.dto.order.CreateOrderLineDto;
import com.gamesup.api.dto.order.OrderDto;
import com.gamesup.api.enumeration.OrderStatus;
import com.gamesup.api.exception.ResourceNotFoundException;
import com.gamesup.api.model.*;
import com.gamesup.api.repository.GameRepository;
import com.gamesup.api.repository.OrderRepository;
import com.gamesup.api.repository.UserRepository;
import com.gamesup.api.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private GameRepository gameRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        userRepository = mock(UserRepository.class);
        gameRepository = mock(GameRepository.class);
        orderService = new OrderService(orderRepository, userRepository, gameRepository);
    }

    @Test
    void createOrder_shouldCreateOrderSuccessfully() {
        Long userId = 1L;
        Long gameId = 100L;
        User user = new User();
        user.setId(userId);

        Game game = new Game();
        game.setId(gameId);
        game.setName("Game1");
        game.setPrice(new BigDecimal("29.99"));

        CreateOrderLineDto lineDto = new CreateOrderLineDto(gameId);
        CreateOrderDto createOrderDto = new CreateOrderDto("1 street town", List.of(lineDto));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderDto result = orderService.createOrder(userId, createOrderDto);

        assertEquals(OrderStatus.PENDING, result.status());
        assertEquals("1 street town", result.shippingAddress());
        assertEquals(new BigDecimal("29.99"), result.totalPrice());
        assertEquals(1, result.lines().size());
        assertEquals(game.getId(), result.lines().getFirst().gameId());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_shouldThrowWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        CreateOrderDto dto = new CreateOrderDto("1 street town", List.of());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(1L, dto));
    }

    @Test
    void createOrder_shouldThrowWhenGameNotFound() {
        Long userId = 1L;
        Long gameId = 999L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        CreateOrderLineDto lineDto = new CreateOrderLineDto(gameId);
        CreateOrderDto dto = new CreateOrderDto("1 street town", List.of(lineDto));

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(userId, dto));
    }

    @Test
    void getOrdersByUser_shouldReturnList() {
        Long userId = 1L;

        Game game = new Game();
        game.setId(100L);
        game.setName("Game1");
        game.setPrice(new BigDecimal("10.00"));

        Order order = new Order();
        order.setId(1L);
        order.setDate(LocalDate.now());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress("1 street town");
        order.setTotalPrice(new BigDecimal("10.00"));

        OrderLine orderLine = new OrderLine();
        orderLine.setGame(game);
        orderLine.setOrder(order);

        order.setLines(Set.of(orderLine));

        when(orderRepository.findByUserId(userId)).thenReturn(List.of(order));

        List<OrderDto> result = orderService.getOrdersByUser(userId);

        assertEquals(1, result.size());
        assertEquals("Game1", result.getFirst().lines().getFirst().gameName());
    }

    @Test
    void getOrderById_shouldReturnOrder_whenUserOwnsIt() {
        Long userId = 1L;
        Order order = new Order();
        order.setId(1L);
        order.setUser(new User());
        order.getUser().setId(userId);
        order.setLines(Set.of());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDto dto = orderService.getOrderById(1L, userId);

        assertEquals(1L, dto.id());
    }

    @Test
    void getOrderById_shouldThrow_whenUserIsNotOwner() {
        Order order = new Order();
        User user = new User();
        user.setId(2L);
        order.setUser(user);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L, 1L));
    }

    @Test
    void getAllOrders_shouldReturnAll() {
        Order order = new Order();
        order.setId(1L);
        order.setLines(Set.of());

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderDto> result = orderService.getAllOrders();

        assertEquals(1, result.size());
    }

    @Test
    void getAdminOrderById_shouldReturnOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setLines(Set.of());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDto result = orderService.getAdminOrderById(1L);

        assertEquals(1L, result.id());
    }

    @Test
    void getAdminOrderById_shouldThrowIfNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getAdminOrderById(1L));
    }
}