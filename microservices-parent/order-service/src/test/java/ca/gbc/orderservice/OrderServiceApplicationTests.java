package ca.gbc.orderservice;

import ca.gbc.orderservice.controller.OrderController;
import ca.gbc.orderservice.dto.OrderLineItemDto;
import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.model.Order;
import ca.gbc.orderservice.repository.OrderRepository;
import ca.gbc.orderservice.service.OrderServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderServiceApplicationTests {

	@Container
	public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("order-service")
			.withUsername("admin")
			.withPassword("password");

	@BeforeAll
	static void beforeAll() {
		postgreSQLContainer.start();
	}

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderServiceImpl orderService;

	@Test
	void placeOrder(){
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderLineItemDtoList(new ArrayList<>());

		OrderLineItemDto item1 = new OrderLineItemDto();
		item1.setPrice(new BigDecimal("10.00"));
		item1.setQuantity(2);
		item1.setSkuCode("QWE123");

		orderRequest.getOrderLineItemDtoList().add(item1);

		orderService.placeOrder(orderRequest);
		List<Order> savedOrders = orderRepository.findAll();
		Order savedOrder = orderRepository.findAll().get(savedOrders.size()-1);
		assert savedOrder.getOrderNumber() != null;
		assert savedOrder.getOrderLineItemList().size() == 1;

	}
}
