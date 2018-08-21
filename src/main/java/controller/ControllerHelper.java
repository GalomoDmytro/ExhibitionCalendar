package controller;

import controller.command.*;
import controller.command.moderatorCommand.*;
import controller.command.user.UserHome;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class ControllerHelper {

    private Map<String, Command> commandMap = new HashMap<>();

    ControllerHelper() {
        initCommandMap();
    }

    Command getCommand(HttpServletRequest req, HttpServletResponse resp) {
        String commandName = req.getParameter("command");

        if (commandName != null) {
            return commandMap.get(commandName);
        }

        return new Home();
    }

    private void initCommandMap() {
        commandMap.put("home", new Home());
        commandMap.put("login", new LoginCommand());
        commandMap.put("registration", new RegistrationCommand());
        commandMap.put("logout", new LogoutCommand());
        commandMap.put("admin", new Admin());
        commandMap.put("moderatorHome", new ModeratorHome());
        commandMap.put("addExpoCenter", new AddingExpoCenter());
        commandMap.put("expoCenterManagement", new ExpoCenterManagement());
        commandMap.put("editExpositionCenter", new EditCenter());
        commandMap.put("addExposition", new AddExpo());
        commandMap.put("expoManagement", new ExhibitionManagement());
        commandMap.put("editExposition", new EditExposition());
        commandMap.put("combineExpoWithCenter", new combineExhibitionWithExhibionCenter());
        commandMap.put("createContract", new CreateContract());
        commandMap.put("contractManagement", new ContractManagement());
        commandMap.put("editContract", new EditContract());
        commandMap.put("purchase", new Purchase());
        commandMap.put("expoInfo", new ExpoInfo());
        commandMap.put("userHome", new UserHome());
    }

}
