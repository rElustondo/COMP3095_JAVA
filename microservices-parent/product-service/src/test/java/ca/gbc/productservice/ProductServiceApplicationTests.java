package ca.gbc.productservice;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.model.Product;
import ca.gbc.productservice.repository.ProductRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests extends AbstractContainerBaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    private ProductRequest getProductRequest(){
        return ProductRequest.builder()
                .name("Apple iPad 2023")
                .description("Apple Ipad version 2023")
                .price(BigDecimal.valueOf(1200))
                .build();
    }

    private List<Product> getProducts(){
        List<Product> products = new ArrayList<>();
        UUID uuid = UUID.randomUUID();
        Product product = Product.builder()
                .id(uuid.toString())
                .name("Apple iPad 2023")
                .description("Apple Ipad version 2023")
                .price(BigDecimal.valueOf(1200))
                .build();
        products.add(product);
        return products;
    }

    private String convertObjectToString(List<ProductResponse> productList) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(productList);
    }

    private List<ProductResponse> convertStringToObject(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, new TypeReference<List<ProductResponse>>() {});
    }

    @Test
    void  createProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();
        String productRequestJsonString = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequestJsonString)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        //Assertions
        Assertions.assertTrue(productRepository.findAll().size()>0);

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("Apple iPad 2023"));
        List<Product> product = mongoTemplate.find(query,Product.class);
        Assertions.assertTrue(product.size() == 1);
    }

}
