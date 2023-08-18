package sample.cafekiosk.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.api.controller.product.ProductController;
import sample.cafekiosk.spring.api.service.product.ProductService;

@WebMvcTest(controllers = ProductController.class)
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean //컨테이너에 mock객체를 넣어준다.
    protected ProductService productService;

    @Autowired
    protected ObjectMapper objectMapper;
}
