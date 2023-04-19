package com.sulzhenko.controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * LocaleFilter  class. Sets and changes locale
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
@WebFilter(urlPatterns = { "/*" },
        initParams = {
                @WebInitParam(name = "defaultLocale", value = "en") })
public class LocaleFilter implements Filter {
    private static final String LOCALE = "locale";
    private static final String REFRESH = "Refresh";
    private static final int REFRESH_TIME = 0;
    private String defaultLocale;

    /**
     * Sets default locale
     * @param config passed by application
     */
    @Override
    public void init(FilterConfig config) {
        defaultLocale = config.getInitParameter("defaultLocale");
    }

    /**
     * Checks if request contains locale parameter and sets locale to session as attribute if present.
     * Returns previous page in such case.
     * In other case checks if locale presents in session. If not sets default locale.
     * doFilter after that.
     * @param request passed by application
     * @param response passed by application
     * @param chain passed by application
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String locale = httpRequest.getParameter(LOCALE);
        if (isNotBlank(locale)) {
            httpRequest.getSession().setAttribute(LOCALE, locale);
            ((HttpServletResponse)response).setIntHeader(REFRESH, REFRESH_TIME);
            if ((!httpRequest.getServletPath().contains("controller"))) {
                chain.doFilter(request, response);
            }
        } else {
            String sessionLocale = (String) httpRequest.getSession().getAttribute(LOCALE);
            if (isBlank(sessionLocale)) {
                httpRequest.getSession().setAttribute(LOCALE, defaultLocale);
            }
            chain.doFilter(request, response);
        }
    }

    private boolean isBlank(String locale) {
        return locale == null || locale.isEmpty();
    }

    private boolean isNotBlank(String locale) {
        return !isBlank(locale);
    }
}