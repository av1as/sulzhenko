package controller.commands;

import com.sulzhenko.controller.command.ProfileInfoCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import static controller.ControllerTestUtils.getUserDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProfileInfoCommandTests {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    @Test
    void executeTest(){
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(getUserDTO());
        assertEquals("/jsp/profile.jsp", new ProfileInfoCommand().execute(request, response));
    }
}
