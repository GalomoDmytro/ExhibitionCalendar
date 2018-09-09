package controller;

import controller.command.*;
import controller.command.admin.Admin;
import controller.command.common.*;
import controller.command.moderatorCommand.*;
import controller.command.user.UserHome;
import controller.command.util.ChangeLanguage;
import controller.command.util.LogoutCommand;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Accepts input and converts it to commands for the model
 */
public class ControllerHelper {

    private static final Logger LOGGER = Logger.getLogger(ControllerHelper.class);

    /**
     * Searches the servlet responsible for the command received from the req
     *
     * @param req
     * @param resp
     * @return Command
     */
    Command getCommand(HttpServletRequest req, HttpServletResponse resp) {
        String commandName = req.getParameter("command");

            return commandsMapping(commandName);
    }

    private Command commandsMapping(String commandName) {
        LOGGER.info("in go  " + commandName);
        switch (commandName) {
            case "home":
                return new Home();

            case "login":
                return new LoginCommand();

            case "registration":
                return new RegistrationCommand();

            case "logout":
                return new LogoutCommand();

            case "admin":
                return new Admin();

            case "moderatorHome":
                return new ModeratorHome();

            case "addExpoCenter":
                return new AddingExpoCenter();

            case "expoCenterManagement":
                return new ExpoCenterManagement();

            case "editExpositionCenter":
                return new EditCenter();

            case "addExposition":
                return new AddExpo();

            case "expoManagement":
                return new ExhibitionManagement();

            case "editExposition":
                return new EditExposition();

            case "combineExpoWithCenter":
                return new CombineExWithExCenter();

            case "createContract":
                return new ContractManagement();

            case "contractManagement":
                return new CreateContract();

            case "editContract":
                return new EditContract();

            case "purchase":
                return new Purchase();

            case "expoInfo":
                return new ExpoInfo();

            case "userHome":
                return new UserHome();

            case "changeLang":
                return new ChangeLanguage();

            case "checkOut":
                return new CheckOut();

            case "purchaseProcessing":
                return new PurchaseProcessing();

            case "waitApprovalTicket":
                return new WaitApprovalTicket();

            case "approvedTicket":
                return new ApprovedTickets();

            default:
                return new Home();
        }
    }

}
