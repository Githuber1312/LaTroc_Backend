package dev.artep.latroc_backend_simple.config;

import dev.artep.latroc_backend_simple.model.Category;
import dev.artep.latroc_backend_simple.model.LocalUser;
import dev.artep.latroc_backend_simple.repository.LocalCategoryDAO;
import dev.artep.latroc_backend_simple.repository.LocalUserDAO;
import dev.artep.latroc_backend_simple.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class AutoTableDataFiller {

    private final LocalUserDAO userRepository;
    private final LocalCategoryDAO categoryRepository;

    public AutoTableDataFiller(LocalUserDAO userRepository, LocalCategoryDAO localCategoryDAO, UserService userService) {
        this.userRepository = userRepository;
        this.categoryRepository = localCategoryDAO;
    }

    @PostConstruct
    public void seedData() {
        createUsers();
        createCategories();
    }

    private void createUsers() {
        if (userRepository.findByUsernameIgnoreCase("user1").isEmpty()) {
            LocalUser user1 = new LocalUser();
            user1.setUsername("user1");
            user1.setPassword("password1");
            user1.setEmail("user1@mail.com");
            userRepository.save(user1);
        }
        if (userRepository.findByUsernameIgnoreCase("user2").isEmpty()) {
            LocalUser user2 = new LocalUser();
            user2.setUsername("user2");
            user2.setPassword("password2");
            user2.setEmail("user2@mail.com");
            userRepository.save(user2);
        }
        if (userRepository.findByUsernameIgnoreCase("user3").isEmpty()) {
            LocalUser user3 = new LocalUser();
            user3.setUsername("user3");
            user3.setPassword("password3");
            user3.setEmail("user3@mail.com");
            userRepository.save(user3);
        }
        if (userRepository.findByUsernameIgnoreCase("user4").isEmpty()) {
            LocalUser user4 = new LocalUser();
            user4.setUsername("user4");
            user4.setPassword("password4");
            user4.setEmail("user4@mail.com");
            userRepository.save(user4);
        }
        if (userRepository.findByUsernameIgnoreCase("user5").isEmpty()) {
            LocalUser user5 = new LocalUser();
            user5.setUsername("user5");
            user5.setPassword("password5");
            user5.setEmail("user5@mail.com");
            userRepository.save(user5);
        }
    }


    private void createCategories() {
        categoryRepository.save(new Category("Electronics"));
        categoryRepository.save(new Category("Components"));
        categoryRepository.save(new Category("Bicycles"));
        categoryRepository.save(new Category("Books"));
        categoryRepository.save(new Category("Clothing"));
        categoryRepository.save(new Category("Toys"));
        categoryRepository.save(new Category("Home"));
        categoryRepository.save(new Category("Furniture"));
        categoryRepository.save(new Category("Sports"));
        categoryRepository.save(new Category("Music"));
        categoryRepository.save(new Category("Beauty"));
        categoryRepository.save(new Category("Garden"));
        categoryRepository.save(new Category("Pet"));
        categoryRepository.save(new Category("Jewelry"));
        categoryRepository.save(new Category("Health"));
        categoryRepository.save(new Category("Office"));
        categoryRepository.save(new Category("Groceries"));
        categoryRepository.save(new Category("Footwear"));
    }

}
