package com.gamesUP.api.model;

import com.gamesup.api.enumeration.OrderStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal totalPrice;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;

	@Column(nullable = false)
	private String shippingAddress;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderLine> lines = new HashSet<>();
}
