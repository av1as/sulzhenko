package com.sulzhenko.controller.filters;

import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.controller.filters.domain.Domain;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * AuthenticationFilter class. Controls access to pages for anonymous user
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
@WebFilter(urlPatterns = { "/*" })
public class AuthenticationFilter implements Filter, Constants, Path {

    /**
     * Checks user in session and then checks if user has access to page or action.
     * @param request passed by application
     * @param response passed by application
     * @param chain passed by application
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (isAccessDenied(httpRequest)) {
            httpRequest.setAttribute(ERROR, ACCESS_DENIED);
            request.setAttribute(PATH, httpRequest.getServletPath());
            request.getRequestDispatcher(PAGE_ERROR).forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }
    private boolean isAccessDenied(HttpServletRequest request) {
        UserDTO userDTO = (UserDTO) request.getSession().getAttribute(USER);
        String role = UNKNOWN;
        if(userDTO != null) {
            role = String.valueOf(userDTO.getRole());
        }
        return Domain.getDomain(request.getServletPath(), request.getParameter(ACTION), role).checkAccess();
    }
}