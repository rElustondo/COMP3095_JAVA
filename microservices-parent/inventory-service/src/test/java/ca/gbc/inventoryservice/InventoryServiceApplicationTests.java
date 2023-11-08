package ca.gbc.inventoryservice;

import ca.gbc.inventoryservice.dto.InventoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryServiceApplicationTests {


    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("inventory-service")
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
    void checkInventory(){
        List<InventoryRequest> inventoryRequests = new ArrayList<>();
        InventoryRequest request = new InventoryRequest();
        request.setSkuCode("QWE123");
        request.setQuantity(2);
        inventoryRequests.add(request);

        try {
            mockMvc.perform(post("/api/inventory")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inventoryRequests)))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Perform database-related assertions as needed
        try (Connection connection = postgreSQLContainer.createConnection("");
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM inventory WHERE sku_code = 'QWE123'");
            while (resultSet.next()) {
                int quantity = resultSet.getInt("quantity");
                assertNotNull(quantity);
                assertTrue(quantity >= 1); // Adjust the condition as needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
