package app.codeodyssey.codeodysseyapi.token.api;

import app.codeodyssey.codeodysseyapi.common.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RefreshTokenEndpointTest.class)
class RefreshTokenEndpointTest {

    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;
    

}