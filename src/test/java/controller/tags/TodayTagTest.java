package controller.tags;

import com.sulzhenko.controller.tag.TodayTag;
import jakarta.servlet.jsp.JspContext;
import jakarta.servlet.jsp.JspWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TodayTagTest {

    @Test
    void testDoTag() throws IOException {
        try(JspWriter jspWriter = new MyJspWriter()) {
            JspContext jspContext = mock(JspContext.class);
            when(jspContext.getOut()).thenReturn(jspWriter);
            TodayTag todayTag = new TodayTag();
            todayTag.setJspContext(jspContext);
            assertDoesNotThrow(todayTag::doTag);
            assertEquals(LocalDate.now().toString(), jspWriter.toString());
        }
    }

    private static class MyJspWriter extends JspWriter {

        private final StringWriter stringWriter;

        public MyJspWriter() {
            super(4096, true);
            stringWriter = new StringWriter();
        }

        @Override
        public void print(Object o) {
            stringWriter.write(o.toString());
        }

        @Override
        public String toString() {
            return stringWriter.toString();
        }

        @Override
        public void newLine() {}
        @Override
        public void print(boolean b) {}
        @Override
        public void print(char c) {}
        @Override
        public void print(int i) {}
        @Override
        public void print(long l) {}
        @Override
        public void print(float v) {}
        @Override
        public void print(double v) {}
        @Override
        public void print(char[] chars) {}
        @Override
        public void print(String s) {}
        @Override
        public void println() {}
        @Override
        public void println(boolean b) {}
        @Override
        public void println(char c) {}
        @Override
        public void println(int i) {}
        @Override
        public void println(long l) {}
        @Override
        public void println(float v) {}
        @Override
        public void println(double v) {}
        @Override
        public void println(char[] chars) {}
        @Override
        public void println(String s) {}
        @Override
        public void println(Object o) {}
        @Override
        public void clear() {}
        @Override
        public void clearBuffer() {}
        @Override
        public void flush() {}
        @Override
        public void close() {}
        @Override
        public int getRemaining() {
            return 0;
        }
        @Override
        public void write(char[] charBuffer, int off, int len) {}
    }
}