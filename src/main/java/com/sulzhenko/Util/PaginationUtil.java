package com.sulzhenko.Util;

import com.sulzhenko.model.Constants;
import jakarta.servlet.http.HttpServletRequest;

public final class PaginationUtil implements Constants {

    public static void paginate(int totalRecords, HttpServletRequest request) {
        int records = getInt(request.getParameter(RECORDS), 1, 5);
        int page = getInt(request.getParameter(PAGE), 1, 1);
        int offset = (page - 1) * records;
        setAttributes(request, totalRecords, records, offset);
    }

    private static void setAttributes(HttpServletRequest request, int totalRecords, int records, int offset) {
        int noOfPages = totalRecords / records + (totalRecords % records != 0 ? 1 : 0);
        request.setAttribute(RECORDS, records);
        request.setAttribute(OFFSET, offset);
        request.setAttribute(NO_OF_PAGES, noOfPages);
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