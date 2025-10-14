package com.gamesup.api.service;

import com.gamesup.api.dto.order.*;
import com.gamesup.api.enumeration.OrderStatus;
import com.gamesup.api.exception.ResourceNotFoundException;
import com.gamesup.api.model.*;
import com.gamesup.api.repository.GameRepository;
import com.gamesup.api.repository.OrderRepository;
import com.gamesup.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, GameRepository gameRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public OrderDto createOrder(Long userId, CreateOrderDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        Order order = new Order();
        order.setDate(LocalDate.now());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(dto.shippingAddress());
        order.setUser(user);

        List<OrderLine> orderLines = dto.lines().stream()
                .map(lineDto -> {
                    Game game = gameRepository.findById(lineDto.gameId())
                            .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable avec l'identifiant " + lineDto.gameId()));
                    OrderLine ol = new OrderLine();
                    ol.setGame(game);
                    ol.setOrder(order);
                    return ol;
                })
                .toList();

        order.setLines(Set.copyOf(orderLines));

        BigDecimal total = orderLines.stream()
                .map(ol -> ol.getGame().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(total);

        Order saved = orderRepository.save(order);

        return toDto(saved);
    }

    public List<OrderDto> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .toList();
    }

    public OrderDto getOrderById(Long id, Long userId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable"));

        if (!order.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Accès refusé à cette commande");
        }

        return toDto(order);
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public OrderDto getAdminOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable"));

        return toDto(order);
    }

    private OrderDto toDto(Order order) {
        List<OrderLineDto> lines = order.getLines().stream()
                .map(ol -> new OrderLineDto(ol.getGame().getId(), ol.getGame().getName()))
                .toList();

        return new OrderDto(
                order.getId(),
                order.getDate(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getShippingAddress(),
                lines
        );
    }
}