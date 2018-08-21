package controller.filter;

import controller.command.Links;
import entities.Role;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"},
        initParams = {
                @WebInitParam(name = "INDEX_PATH", value = "/index.jsp")
        })
public class AcessSecurityFilter implements Filter {
    private String indexPath;
    private FilterConfig filterConfig;

    private static final Logger LOGGER = Logger.getLogger(AcessSecurityFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        indexPath = filterConfig.getInitParameter("INDEX_PATH");
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!allowAccess(httpRequest)) {
            httpRequest.setAttribute("command", httpRequest.getParameter("command"));

            httpRequest.getRequestDispatcher(Links.INDEX_PAGE).forward(httpRequest, httpResponse);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private boolean allowAccess(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String command = req.getParameter("command");

        if (command != null) {
            switch (command) {
                case "home":
                case "login":
                case "registration":
                case "logout":
                case "purchase":
                case "expoInfo":
                    return true;

                case "moderatorHome":
                case "addExpoCenter":
                case "expoCenterManagement":
                case "editExpositionCenter":
                case "addExposition":
                case "expoManagement":
                case "editExposition":
                case "combineExpoWithCenter":
                case "createContract":
                case "contractManagement":
                case "editContract":
                    if (session.getAttribute("role") != null) {
                        if (session.getAttribute("role").equals(Role.MODERATOR)
                                || session.getAttribute("role").equals(Role.ADMIN)) {
                            return true;
                        }
                    }
                    break;

                case "admin":
                    if (session.getAttribute("role") != null) {
                        if (session.getAttribute("role").equals(Role.ADMIN)) {
                            return true;
                        }
                    }
                    break;

                case "userHome":
                    if (session.getAttribute("role") != null) {
                        if (session.getAttribute("role").equals(Role.MODERATOR)
                                || session.getAttribute("role").equals(Role.ADMIN)
                                || session.getAttribute("role").equals(Role.USER)) {
                            return true;
                        }
                    }
                    break;
            }
        }

        return false;
    }
}
