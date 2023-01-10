package com.sulzhenko.model.Util;

import jakarta.servlet.http.HttpServletRequest;

public final class PaginationUtil {

    public static void paginate(int totalRecords, HttpServletRequest request) {
        int records = getInt(request.getParameter("records"), 1, 5);
        int page = getInt(request.getParameter("page"), 1, 1);
        int offset = (page - 1) * records;
        setAttributes(request, totalRecords, records, offset);
    }

    private static void setAttributes(HttpServletRequest request, int totalRecords, int records, int offset) {
        int noOfPages = totalRecords / records + (totalRecords % records != 0 ? 1 : 0);
        request.setAttribute("records", records);
        request.setAttribute("offset", offset);
        request.setAttribute("noOfPages", noOfPages);
    }

    private static int getInt(String value, int min, int defaultValue) {
        try {
            int records = Integer.parseInt(value);
            if (records >= min) {
                return records;
            }
        } catch (NumberFormatException e) {
            return defaultValue;
        }
        return defaultValue;
    }

    private PaginationUtil() {}
}