package ca.gbc.orderservice;

import ca.gbc.orderservice.controller.OrderController;
import ca.gbc.orderservice.dto.OrderLineItemDto;
import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.model.Order;
import ca.gbc.orderservice.repository.OrderRepository;
import ca.gbc.orderservice.service.OrderServiceImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class OrderServiceApplicationTests {
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

		Order savedOrder = orderRepository.findAll().get(0);
		assert savedOrder.getOrderNumber() != null;
		assert savedOrder.getOrderLineItemList().size() == 1;

	}
}
