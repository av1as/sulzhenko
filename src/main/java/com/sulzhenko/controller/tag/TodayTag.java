package com.sulzhenko.controller.tag;

import jakarta.servlet.jsp.*;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;
import java.time.LocalDate;

/**
 * TodayTag  class.
 *
 */
public class TodayTag extends SimpleTagSupport {

    /**
     *  Writes to JspWriter formatted current date.
     */
    @Override
    public void doTag() throws IOException {
        final JspWriter writer = getJspContext().getOut();
        writer.print(LocalDate.now());
    }
}