package ma.sdsi.gestionressources.config;

import ma.sdsi.gestionressources.services.DefaultUserService;
import ma.sdsi.gestionressources.services.DefaultUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/css/**", "/js/**", "/webjars/**", "/img/**").permitAll() // Permettre les ressources statiques
                                .requestMatchers("/login", "/registration").permitAll() // Chemins publics
                                .requestMatchers("/responsable/**").hasAuthority("RESPONSABLE")
                                .requestMatchers("/propositions").hasAuthority("RESPONSABLE")// Responsable accès aux chemins de responsable
                                .requestMatchers("/chefdepartement/**").hasAuthority("CHEF DEPARTEMENT")
                                .requestMatchers("/signalerPanneChef").hasAuthority("CHEF DEPARTEMENT") // Accès pour le chef de département
                                .requestMatchers("/index").hasAuthority("CHEF DEPARTEMENT")
                                .requestMatchers("/consulterBesoinDepartement/**").hasAuthority("CHEF DEPARTEMENT")
                                .requestMatchers("/consulterBesoinEnseignant/**").hasAuthority("CHEF DEPARTEMENT")
                                .requestMatchers("/enseignant/**").hasAuthority("ENSEIGNANT") // Accès pour les enseignants
                                .requestMatchers("/technicien/remplir-constat").hasAuthority("TECHNICIEN") // Accès pour les techniciens
                                .requestMatchers("/remplir-constat").hasAuthority("TECHNICIEN")
                                .requestMatchers("/consulterRessources/**").hasAuthority("FOURNISSEUR") // Accès pour les fournisseurs
                                .requestMatchers("/proposer/**").hasAuthority("FOURNISSEUR") // Accès pour les fournisseurs
                                .anyRequest().authenticated() // Toutes les autres requêtes nécessitent une authentification
                )
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .successHandler(customSuccessHandler) // Utilise le gestionnaire personnalisé
                                .permitAll()
                )

                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll()
                )
                .sessionManagement(session ->
                        session
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Politique de session
                );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new DefaultUserServiceImpl(); // Remplacez par votre implémentation
    }
}
