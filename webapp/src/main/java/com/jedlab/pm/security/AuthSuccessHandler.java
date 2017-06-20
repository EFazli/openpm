package com.jedlab.pm.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.jedlab.pm.model.User;

/**
 * @author omidp
 *
 */
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler
{

    public static final String CURRENT_USERNAME = "currentUsername";
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        if(authentication != null && authentication.isAuthenticated())
        {
            Object principal = authentication.getPrincipal();
            //TODO:check for anonymouse
            if(principal instanceof User)
            {
                HttpSession session = request.getSession(false);
                if(session != null)
                    session.setAttribute(CURRENT_USERNAME, ((User)principal).getUsername());
            }
        }
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

}