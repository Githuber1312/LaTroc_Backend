package dev.artep.latroc_backend_simple.service;

import dev.artep.latroc_backend_simple.dto.LoginBody;
import dev.artep.latroc_backend_simple.dto.RegistrationBody;
import dev.artep.latroc_backend_simple.dto.UserResponse;
import dev.artep.latroc_backend_simple.exception.UserNotFoundException;
import dev.artep.latroc_backend_simple.model.LocalUser;
import dev.artep.latroc_backend_simple.model.Post;
import dev.artep.latroc_backend_simple.repository.LocalPostDAO;
import dev.artep.latroc_backend_simple.repository.LocalUserDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private LocalUserDAO localUserDAO;
    private LocalPostDAO localPostDAO;
    private JWTService jwtService;

    public UserService(LocalUserDAO localUserDAO, LocalPostDAO localPostDAO, JWTService jwtService) {
        this.localUserDAO = localUserDAO;
        this.localPostDAO = localPostDAO;
        this.jwtService = jwtService;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws Exception{
        if (localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()
            || localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()){
            throw new Exception("User is already registered");
        }
        LocalUser localUser = new LocalUser();
        localUser.setUsername(registrationBody.getUsername());
        localUser.setPassword(registrationBody.getPassword());
        localUser.setEmail(registrationBody.getEmail()); //TODO encrypt password

        localUser = localUserDAO.save(localUser);
        return localUser;
    }

    public String loginUser(LoginBody loginBody) {

        Optional<LocalUser> localUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if (localUser.isPresent()){
            LocalUser lUser = localUser.get();
            if (localUser.get().getPassword().equals(loginBody.getPassword())){
                return jwtService.generateJWT(lUser);
            }
        }
        return null;
    }

    public LocalUser getUserById(Long id) {
        return localUserDAO.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public Long getUserIdByUsername(String username) {
        return localUserDAO.findByUsernameIgnoreCase(username)
                .map(LocalUser::getId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponse getUserDetailsWithPosts(Long id) {
        LocalUser user = getUserById(id);
        List<Post> posts = localPostDAO.findByUserId(id);
        return new UserResponse(user, posts);
    }
}
