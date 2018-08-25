package controller.filter;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(
//        urlPatterns = {"/*"},
        filterName = "XSSFilter"
)
public class XSSFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(EncodingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
    }
}
