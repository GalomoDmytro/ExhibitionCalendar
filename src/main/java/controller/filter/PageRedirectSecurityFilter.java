package controller.filter;

import controller.command.util.Links;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Prohibits access through the address bar
 */

@WebFilter(
//        urlPatterns = {"/views/*"},
        filterName = "PageRedirectSecurityFilter",
        initParams = {
                @WebInitParam(name = "INDEX_PATH", value = "/index.jsp")
        })
public class PageRedirectSecurityFilter implements Filter {
    private String indexPath;
    private static final Logger LOGGER = Logger.getLogger(PageRedirectSecurityFilter.class);

    public void init(FilterConfig fConfig) throws ServletException {
        indexPath = fConfig.getInitParameter("INDEX_PATH");
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        RequestDispatcher dispatcher = request.getRequestDispatcher(Links.HOME_PAGE);
        dispatcher.forward(httpRequest, httpResponse);
    }

    public void destroy() {
    }


}
