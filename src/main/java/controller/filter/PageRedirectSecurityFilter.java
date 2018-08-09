package controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter(urlPatterns = {"/*"},
//        initParams = {@WebInitParam(name = "INDEX_PATH", value = "/views/home.jsp")})
public class PageRedirectSecurityFilter implements Filter {
    private String indexPath;

    public void init(FilterConfig fConfig) throws ServletException {
        indexPath = fConfig.getInitParameter("INDEX_PATH");
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(indexPath);
        requestDispatcher.forward(request, response);
//        httpResponse.sendRedirect(httpRequest.getContextPath() + indexPath);
//        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}
