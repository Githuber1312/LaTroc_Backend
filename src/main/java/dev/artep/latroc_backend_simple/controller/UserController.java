package dev.artep.latroc_backend_simple.controller;

import dev.artep.latroc_backend_simple.dto.LoginBody;
import dev.artep.latroc_backend_simple.dto.RegistrationBody;
import dev.artep.latroc_backend_simple.dto.LoginResponse;
import dev.artep.latroc_backend_simple.dto.UserResponse;
import dev.artep.latroc_backend_simple.exception.PostNotFoundException;
import dev.artep.latroc_backend_simple.service.JWTService;
import dev.artep.latroc_backend_simple.service.UserService;
import dev.artep.latroc_backend_simple.exception.UserAlreadyLoggedInException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private UserService userService;
    private JWTService jwtService;

    public UserController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody @Valid RegistrationBody registrationBody) {
        try {
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestHeader(value = "Authorization", required = false) String token,
                                                   @Valid @RequestBody LoginBody loginBody) throws Exception {
        if (token != null) {
            String parsedToken = token.replace("Bearer ", "").split(";")[0];
            System.out.println("Parsed token: " + parsedToken);

            if (jwtService.validateToken(parsedToken)) {
                throw new UserAlreadyLoggedInException("User is already logged in!");
            }
        }

        String jwt = userService.loginUser(loginBody);
        if (jwt == null) {
            System.out.println("Login FAILED");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            System.out.println("User logged in.");
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);

            Long userId = userService.getUserIdByUsername(loginBody.getUsername());
            response.setUserId(userId);
            response.setUsername(loginBody.getUsername());
            return ResponseEntity.ok(response);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userService.getUserDetailsWithPosts(id);
        return ResponseEntity.ok(userResponse);
    }
}
