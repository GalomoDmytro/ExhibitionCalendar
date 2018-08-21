package controller.command;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ChangeLanguage implements Command {

    private static final String ruBundle = "strings_ru";
    private static final String engBundle = "strings_eng";

    private static final Logger LOGGER = Logger.getLogger(ChangeLanguage.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String url = req.getHeader("referer");

        HttpSession session = req.getSession();
        if (session.getAttribute("langBundle") == null) {
            session.setAttribute("langBundle", ruBundle);
        } else if (session.getAttribute("langBundle") == ruBundle) {
            session.setAttribute("langBundle", engBundle);

        } else {
            session.setAttribute("langBundle", ruBundle);
        }

        resp.sendRedirect(url);
    }
}
