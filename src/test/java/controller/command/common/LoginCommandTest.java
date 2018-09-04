package controller.command.common;

import static org.mockito.Mockito.*;

import controller.command.Command;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCommandTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    RequestDispatcher rd;

    @Mock
    private HttpSession session;

    Command loginCommand;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute() {

        loginCommand = new LoginCommand();

        when(request.getParameter("nameOrMail")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        try {
            loginCommand.execute(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        verify(request, atLeast(1)).getParameter("nameOrMail");
        verify(request, atLeast(1)).getParameter("password");
    }
}