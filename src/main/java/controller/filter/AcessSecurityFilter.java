package controller.filter;

import controller.command.Links;
import entities.Role;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class AcessSecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        RequestDispatcher dispatcher = req.getRequestDispatcher(Links.INDEX_PAGE);

        if (!allowAccess(req)) {
            dispatcher.forward(req, servletResponse);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
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
