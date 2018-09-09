package controller.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

/**
 * @author Dmytro Galomko
 */
@WebFilter(
        filterName = "EncodingFilter",
        initParams = {
                @WebInitParam(name = "encoding", value = "UTF-8", description = "Encoding Param"),
                @WebInitParam(name = "content", value = "text/html; charset=UTF-8", description = "Content Param")
        })
public class EncodingFilter implements Filter {
    private String code;
    private String content;

    private static final Logger LOGGER = Logger.getLogger(EncodingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        code = filterConfig.getInitParameter("encoding");
        content = filterConfig.getInitParameter("content");
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        String codeRequest = servletRequest.getCharacterEncoding();

        if (code != null && !code.equalsIgnoreCase(codeRequest)) {
            servletRequest.setCharacterEncoding(code);
            servletResponse.setCharacterEncoding(code);
            servletResponse.setContentType(content);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        code = null;
    }
}
