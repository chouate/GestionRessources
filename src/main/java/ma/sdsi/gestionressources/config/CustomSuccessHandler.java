package ma.sdsi.gestionressources.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectUrl = null;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("RESPONSABLE")) {
                redirectUrl = "/responsable";
                break;
            } else if (grantedAuthority.getAuthority().equals("CHEF DEPARTEMENT")) {
                redirectUrl = "/index";
                break;
            } else if (grantedAuthority.getAuthority().equals("ENSEIGNANT")) {
                redirectUrl = "/enseignant";
                break;
            }
            else if (grantedAuthority.getAuthority().equals("TECHNICIEN")) {
                redirectUrl = "/remplir-constat";
                break;
            }
            else if (grantedAuthority.getAuthority().equals("FOURNISSEUR")) {
                redirectUrl = "/toutAppelOffres";
                break;
            }
        }
        if (redirectUrl == null) {
            throw new IllegalStateException();
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
