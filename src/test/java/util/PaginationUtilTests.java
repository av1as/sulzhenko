package util;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.sulzhenko.model.Constants.*;
import static com.sulzhenko.Util.PaginationUtil.paginate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaginationUtilTests {
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @Test
    void testPaginate() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(NO_OF_PAGES));
    }

    @Test
    void testPaginateZeroTotalRecords() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(0, myRequest);
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(0, myRequest.getAttribute(NO_OF_PAGES));
    }

    @Test
    void testPaginateWrongOffset() {
        when(request.getParameter(OFFSET)).thenReturn("wrong");
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(NO_OF_PAGES));
    }

    @Test
    void testPaginateWrongRecords() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn("wrong");
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(NO_OF_PAGES));
    }

    @Test
    void testPaginateNegativeOffset() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(-3));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(NO_OF_PAGES));
    }

    @Test
    void testPaginateNegativeRecords() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(-3));
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(NO_OF_PAGES));
    }

    @Test
    void testPaginateZeroRecords() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(0));
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(NO_OF_PAGES));
    }

    @Test
    void testPaginateNull() {
        when(request.getParameter(OFFSET)).thenReturn(null);
        when(request.getParameter(RECORDS)).thenReturn(null);
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(NO_OF_PAGES));
    }

    @Test
    void testPaginateOffsetNotZero() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(5));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(20, myRequest.getAttribute(NO_OF_PAGES));
    }

    @Test
    void testPaginateSmallerRecords() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(2));
        MyRequest myRequest = new MyRequest(request);
        paginate(100, myRequest);
        assertEquals(2, myRequest.getAttribute(RECORDS));
        assertEquals(50, myRequest.getAttribute(NO_OF_PAGES));
    }

    @Test
    void testPaginateSmallerTotal() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(10, myRequest);
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(2, myRequest.getAttribute(NO_OF_PAGES));
    }

    @Test
    void testPaginateSmallerTotal2() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(5));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(10, myRequest);
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(2, myRequest.getAttribute(NO_OF_PAGES));
    }

    @Test
    void testPaginateSmallerTotal3() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(10));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(15, myRequest);
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(3, myRequest.getAttribute(NO_OF_PAGES));
    }

    @Test
    void testPaginateSmallerTotal4() {
        when(request.getParameter(OFFSET)).thenReturn(String.valueOf(0));
        when(request.getParameter(RECORDS)).thenReturn(String.valueOf(5));
        MyRequest myRequest = new MyRequest(request);
        paginate(5, myRequest);
        assertEquals(5, myRequest.getAttribute(RECORDS));
        assertEquals(1, myRequest.getAttribute(NO_OF_PAGES));
    }

    private static class MyRequest extends HttpServletRequestWrapper {
        private final Map<String, Object> attributes = new HashMap<>();
        public MyRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public void setAttribute(String name, Object object) {
            attributes.put(name, object);
        }

        @Override
        public Object getAttribute(String name) {
            return attributes.get(name);
        }
    }
}