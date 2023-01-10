package com.sulzhenko.filters;

import com.sulzhenko.model.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import static com.sulzhenko.filters.domain.Domain.getDomain;

@WebFilter(urlPatterns = { "/*" })
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (isAccessDenied(httpRequest)) {
            httpRequest.setAttribute("error", "access.denied");
            request.setAttribute("path", httpRequest.getServletPath());
            request.getRequestDispatcher("/jsp/error_page.jsp").forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }
    private boolean isAccessDenied(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        String role = "UNKNOWN";
        if(user != null) {
            role = String.valueOf(user.getRole());
        }
        return getDomain(request.getServletPath(), request.getParameter("action"), role).checkAccess();
    }
}