package com.bookify.books.Bookify.service.userservices;

import com.bookify.books.Bookify.exceptions.roleexceptions.RoleExistsException;
import com.bookify.books.Bookify.exceptions.userexceptions.UserExistsException;
import com.bookify.books.Bookify.shared.interfaces.userinterface.UserRoleInterface;
import com.bookify.books.Bookify.model.dto.userdto.UserRoleDTO;
import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.model.entities.userentities.UserRole;
import com.bookify.books.Bookify.repository.userrepositories.UserRoleRepository;
import com.bookify.books.Bookify.repository.userrepositories.UserRepository;
import com.bookify.books.Bookify.model.mappers.user.RoleDTOsMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleService implements UserRoleInterface {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleDTOsMapper roleDTOsMapper;
    private final static Logger LOG = LoggerFactory.getLogger(UserRoleService.class);

    @Override
    public UserRoleDTO createNewRole(UserRoleDTO userRoleDTO) {
        userRoleRepository
                .findByRoleName(userRoleDTO.getRoleName())
                .ifPresent(role -> {
                    throw new RoleExistsException("Error. Role alrady exists");
                });

        UserRole newRole = roleDTOsMapper.convertRoleDTOToEntity(userRoleDTO);
        UserRole save = userRoleRepository.save(newRole);

        LOG.info("Role created: {}", save);
        return roleDTOsMapper.convertRoleEntityToDTO(save);
    }

    @Override
    public User assignUserRole(String email, String roleName) {
        User searchUser = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserExistsException("user not found"));

        UserRole searchRole = userRoleRepository
                .findByRoleName(roleName)
                .orElseThrow(() ->
                        new RoleExistsException("Role not found"));

        searchUser.setUserRole(searchRole);

        LOG.info("Role assigned: {}", searchRole.getRoleName() + " to user: " + searchUser.getEmail());
        return userRepository.save(searchUser);
    }
}