package controller.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Interface realisation implies executing
 * command defined by ControllerHelper in Controller
 */

public interface Command {
    void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException;
}
