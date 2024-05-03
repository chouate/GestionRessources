package ma.sdsi.gestionressources.services;

import ma.sdsi.gestionressources.DTO.UserRegisteredDTO;
import ma.sdsi.gestionressources.entities.Role;
import ma.sdsi.gestionressources.entities.User;
import ma.sdsi.gestionressources.repositories.RoleRepository;
import ma.sdsi.gestionressources.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DefaultUserServiceImpl implements DefaultUserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;


    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRole()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
    }

    @Override
    public User save(UserRegisteredDTO userRegisteredDTO) {
        Role role = new Role();
        if(userRegisteredDTO.getRole().equals("RESPONSABLE"))
            role = roleRepo.findByRole("RESPONSABLE");
        else if(userRegisteredDTO.getRole().equals("CHEF DEPARTEMENT"))
            role = roleRepo.findByRole("CHEF DEPARTEMENT");
        if(userRegisteredDTO.getRole().equals("ENSEIGNANT"))
            role = roleRepo.findByRole("ENSEIGNANT");
        else if(userRegisteredDTO.getRole().equals("TECHNICIEN"))
            role = roleRepo.findByRole("TECHNICIEN");
        User user = new User();
        user.setEmail(userRegisteredDTO.getEmail_id());
        user.setName(userRegisteredDTO.getName());
        user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
        user.setRole(role);

        return userRepo.save(user);
    }
}
