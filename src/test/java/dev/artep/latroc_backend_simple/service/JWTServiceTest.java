package dev.artep.latroc_backend_simple.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.artep.latroc_backend_simple.model.LocalUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JWTServiceTest {

    private JWTService jwtService;

    @BeforeEach
    public void setUp() {
        jwtService = new JWTService();
        jwtService.algorithmKey = "secretKey";
        jwtService.issuer = "testIssuer";
        jwtService.expiryInSeconds = 3600;
        jwtService.postConstruct();
    }

    @Test
    public void testGenerateJWT() {
        LocalUser user = new LocalUser();
        user.setUsername("testUser");
        String token = jwtService.generateJWT(user);
        assertNotNull(token);
        DecodedJWT decodedJWT = JWT.decode(token);
        assertEquals("testUser", decodedJWT.getClaim("USERNAME").asString());
        assertEquals("testIssuer", decodedJWT.getIssuer());
    }

    @Test
    public void testValidateToken_ValidToken() {
        LocalUser user = new LocalUser();
        user.setUsername("testUser");
        String token = jwtService.generateJWT(user);
        boolean isValid = jwtService.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    public void testValidateToken_InvalidToken() {
        String invalidToken = "invalid.token.here";
        boolean isValid = jwtService.validateToken(invalidToken);
        assertFalse(isValid);
    }

    @Test
    public void testGetUsernameFromToken() {
        LocalUser user = new LocalUser();
        user.setUsername("testUser");
        String token = jwtService.generateJWT(user);
        String username = jwtService.getUsername(token);
        assertEquals("testUser", username);
    }

    @Test
    public void testGenerateJWT_ExpiryCheck() {
        jwtService.expiryInSeconds = 1; // Set to 1 second for testing expiry
        jwtService.postConstruct();
        LocalUser user = new LocalUser();
        user.setUsername("testUser");
        String token = jwtService.generateJWT(user);

        // Pause to allow token to expire
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean isValid = jwtService.validateToken(token);
        assertFalse(isValid);
    }
}
