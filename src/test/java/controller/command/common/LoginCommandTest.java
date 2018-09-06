package controller.command.common;

import static org.mockito.Mockito.*;

import controller.command.Command;
import controller.command.util.Links;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCommandTest {

    private Command loginCommand;

    @Mock
    private HttpSession sessionTest;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    RequestDispatcher rd;

    @Before
    public void setUp() {

        loginCommand = new LoginCommand();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getNameOrMailReq() throws Exception {

        when(request.getParameter("nameOrMail")).thenReturn("test");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        loginCommand.execute(request, response);

        verify(request, atLeast(1)).getParameter("nameOrMail");
    }

    @Test
    public void getPasswordReq() throws Exception {

        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        loginCommand.execute(request, response);

        verify(request, atLeast(1)).getParameter("password");
    }

    @Test
    public void adminLogIn() throws Exception {

        when(request.getParameter("nameOrMail")).thenReturn("Admin");
        when(request.getParameter("password")).thenReturn("Admin1");
        when(request.getParameter("loginBtn")).thenReturn("test");

        when(request.getSession(true)).thenReturn(sessionTest);

        when(request.getRequestDispatcher(Links.HOME_PAGE)).thenReturn(rd);

        loginCommand.execute(request, response);

        verify(request).getRequestDispatcher(Links.HOME_PAGE);
    }

    @Test
    public void wrongPasswordOrUser() throws Exception {

        when(request.getParameter("nameOrMail")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        when(request.getParameter("loginBtn")).thenReturn("");

        when(request.getSession(true)).thenReturn(sessionTest);

        when(request.getRequestDispatcher(Links.LOGIN_PAGE)).thenReturn(rd);

        loginCommand.execute(request, response);

        verify(request).getRequestDispatcher(Links.LOGIN_PAGE);
    }

    @Test
    public void isLogInBtnPressed() throws Exception {

        when(request.getParameter("loginBtn")).thenReturn("");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        loginCommand.execute(request, response);

        verify(request, atLeast(1));
    }


}