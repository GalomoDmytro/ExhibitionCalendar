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
    private String command;

    private static final Logger LOGGER = Logger.getLogger(AcessSecurityFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        getCommand(httpRequest);

        if (!allowAccess(httpRequest)) {
            redirect(httpRequest, httpResponse);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private boolean allowAccess(HttpServletRequest req) {
        HttpSession session = req.getSession();

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

    private void redirect(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        if (command != null) {
            switch (command) {
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
                case "admin":
                case "userHome":
                    req.setAttribute("command", req.getParameter("command"));
                    req.getRequestDispatcher(Links.LOGIN_PAGE).forward(req, resp);
                    break;

                default:
                    req.setAttribute("command", req.getParameter("command"));
                    req.getRequestDispatcher(Links.HOME_PAGE).forward(req, resp);
                    break;
            }
        }
    }

    private void getCommand(HttpServletRequest req) {
        command = req.getParameter("command");
    }

}
