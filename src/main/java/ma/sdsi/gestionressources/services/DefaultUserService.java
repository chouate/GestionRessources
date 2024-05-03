package ma.sdsi.gestionressources.services;

import ma.sdsi.gestionressources.DTO.UserRegisteredDTO;
import ma.sdsi.gestionressources.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface DefaultUserService extends UserDetailsService {
    User save(UserRegisteredDTO userRegisteredDTO);
}
