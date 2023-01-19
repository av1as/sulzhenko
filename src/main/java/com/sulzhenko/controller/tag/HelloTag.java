package com.sulzhenko.controller.tag;

import jakarta.servlet.jsp.*;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;
@SuppressWarnings("serial")
public class HelloTag extends TagSupport {
    private String role;
    public void setRole(String role) {
        this.role = role;
    }
    @Override
    public int doStartTag() throws JspException {
        try {
            String to;
            if ("administrator".equalsIgnoreCase(role)) {
                to = "hello.admin";
            } else {
                to = "hello.user";
            }
            pageContext.getOut().write("<hr/>" + to + "<hr/>");
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}
