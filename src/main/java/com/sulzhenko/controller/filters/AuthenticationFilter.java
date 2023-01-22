package com.sulzhenko.controller.filters;

import com.sulzhenko.DTO.UserDTO;
import com.sulzhenko.controller.Constants;
import com.sulzhenko.controller.Path;
import com.sulzhenko.controller.filters.domain.Domain;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = { "/*" })
public class AuthenticationFilter implements Filter, Constants, Path {
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