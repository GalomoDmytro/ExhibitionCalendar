package controller;

import controller.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class ControllerHelper {
    private Command command;
    private Map<String, Command> commandMap = new HashMap<>();

    ControllerHelper(){
        initCommandMap();
    }

    Command getCommand(HttpServletRequest req, HttpServletResponse resp) {


        return null;
    }

    private void initCommandMap() {

    }


}
