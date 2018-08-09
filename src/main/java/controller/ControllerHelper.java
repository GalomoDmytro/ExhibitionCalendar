package controller;

import controller.command.Command;
import controller.command.HomeCommand;
import controller.command.LoginCommand;
import controller.command.RegistrationCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class ControllerHelper {
    private Command command;
    private Map<String, Command> commandMap = new HashMap<>();

    ControllerHelper() {
        initCommandMap();
    }

    Command getCommand(HttpServletRequest req, HttpServletResponse resp) {
        String commandName = req.getParameter("command");

        if(commandName != null) {
            return commandMap.get(commandName);
        } else return new HomeCommand();
    }

    private void initCommandMap() {
        commandMap.put("home", new HomeCommand());
        commandMap.put("login", new LoginCommand());
        commandMap.put("registration", new RegistrationCommand());
    }


}
