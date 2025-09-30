package com.bookify.books.Bookify.components;

import com.bookify.books.Bookify.model.dto.userdto.UserDTO;
import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.model.entities.userentities.UserRole;
import com.bookify.books.Bookify.repository.userrepositories.UserRoleRepository;
import com.bookify.books.Bookify.repository.userrepositories.UserRepository;
import com.bookify.books.Bookify.model.mappers.user.UserDTOsMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RolesInitialize implements CommandLineRunner, SuperAdmin {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final UserDTOsMapper userDTOsMapper;
    private final PasswordEncoder passwordEncoder;
    private final static Logger LOG = LoggerFactory.getLogger(RolesInitialize.class);

    @Override
    public void run(String... args) throws Exception {
        if (userRoleRepository.count() == 0) {
            UserRole superAdmin = new UserRole();
            superAdmin.setRoleName("SUPER_ADMIN");
            userRoleRepository.save(superAdmin);

            UserRole adminRole = new UserRole();
            adminRole.setRoleName("ADMIN");
            userRoleRepository.save(adminRole);

            UserRole userRole = new UserRole();
            userRole.setRoleName("USER");
            createSuperAdmin();
            userRoleRepository.save(userRole);
        }
    }

    private UserDTO createSuperAdmin() {
        UserDTO superUser = new UserDTO();
        UserRole superAdminRole = userRoleRepository
                .findByRoleName("SUPER_ADMIN")
                .orElseThrow(() ->
                        new RuntimeException("Super admin role not found"));

        superUser.setUserRole(superAdminRole);
        superUser.setName(SUPER_ADMIN_NAME);
        superUser.setEmail(SUPER_ADMIN_EMAIL);
        superUser.setPassword(passwordEncoder.encode(SUPER_ADMIN_PASSWORD));

        User user = userDTOsMapper.convertDTOToEntity(superUser);
        User saveUser = userRepository.save(user);

        LOG.info("Super user created");
        return userDTOsMapper.convertEntityToDTO(saveUser);
    }
}
