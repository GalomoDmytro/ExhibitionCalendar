package controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeTest extends Mockito {

    private Command home;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    RequestDispatcher dispatcher;

    @Before
    public void setUp() {
        home = new Home();
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void getSearchDateReq() throws Exception {

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        home.execute(request, response);

        verify(request, atLeast(1)).getParameter("searchDate");
    }

    @Test
    public void getSearchLineReq() throws Exception{

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        home.execute(request, response);

        verify(request, atLeast(1)).getParameter("searchField");
    }

    @Test
    public void setDataToReq() throws Exception{
        when(request.getParameter("searchField")).thenReturn("test");
        when(request.getParameter("searchDate")).thenReturn("2001-01-01");
        when(request.getParameter("currentPage")).thenReturn("0");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        home.execute(request, response);

        verify(request).setAttribute("currentPage", 0);
        verify(request).setAttribute("searchDateLine", "2001-01-01");
        verify(request).setAttribute("searchDate", "2001-01-01");
        verify(request).setAttribute("searchField", "test");
    }

}