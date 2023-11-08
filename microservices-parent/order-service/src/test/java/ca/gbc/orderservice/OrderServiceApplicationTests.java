package ca.gbc.orderservice;
import org.springframework.http.MediaType;
import ca.gbc.orderservice.controller.OrderController;
import ca.gbc.orderservice.dto.OrderLineItemDto;
import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.model.Order;
import ca.gbc.orderservice.repository.OrderRepository;
import ca.gbc.orderservice.service.OrderServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderServiceApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	@Container
	public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("order-service")
			.withUsername("admin")
			.withPassword("password");

	@BeforeAll
	static void beforeAll() {
		postgreSQLContainer.start();
	}
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Autowired
	private ObjectMapper objectMapper;
	@Test
	void placeOrder(){
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderLineItemDtoList(new ArrayList<>());

		OrderLineItemDto item1 = new OrderLineItemDto();
		item1.setPrice(new BigDecimal("10.00"));
		item1.setQuantity(2);
		item1.setSkuCode("QWE123");

		orderRequest.getOrderLineItemDtoList().add(item1);

		try {
			mockMvc.perform(post("/api/order")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(orderRequest)))
					.andExpect(status().isCreated());
		} catch (Exception e) {
			e.printStackTrace();
		}


		try (Connection connection = postgreSQLContainer.createConnection("");
			 Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM orders");
			while (resultSet.next()) {
				String orderNumber = resultSet.getString("order_number");
				assertNotNull(orderNumber);
				assertTrue(orderNumber.startsWith("QWE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//orderService.placeOrder(orderRequest);
		//List<Order> savedOrders = orderRepository.findAll();
		//Order savedOrder = orderRepository.findAll().get(savedOrders.size()-1);
		//assert savedOrder.getOrderNumber() != null;
		//assert savedOrder.getOrderLineItemList().size() == 1;

	}
}
