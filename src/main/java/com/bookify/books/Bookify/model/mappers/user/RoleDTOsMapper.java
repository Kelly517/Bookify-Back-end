package com.bookify.books.Bookify.model.mappers.user;

import com.bookify.books.Bookify.model.dto.userdto.UserRoleDTO;
import com.bookify.books.Bookify.model.entities.userentities.UserRole;
import org.springframework.stereotype.Component;

@Component
public class RoleDTOsMapper {

    // Convierte un UserRoleDTO en una entidad UserRole (para guardar en BD)
    public UserRole convertRoleDTOToEntity(UserRoleDTO userRoleDTO) {
        return new UserRole(userRoleDTO.getRoleId(), userRoleDTO.getRoleName(), userRoleDTO.getUserEntities());
    }

    // Convierte un UserRole en una entidad UserRoleDTO (para guardar en BD)
    public UserRoleDTO convertRoleEntityToDTO(UserRole userRole) {
        return new UserRoleDTO(userRole.getRoleId(), userRole.getRoleName(), userRole.getUserEntities());
    }
}