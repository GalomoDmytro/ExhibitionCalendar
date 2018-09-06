package controller;

import controller.command.Home;
import controller.command.admin.Admin;
import controller.command.common.*;
import controller.command.moderatorCommand.*;
import controller.command.user.UserHome;
import controller.command.util.ChangeLanguage;
import controller.command.util.LogoutCommand;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ControllerHelperTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getCommandHome() {
        when(request.getParameter("command")).thenReturn("home");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new Home().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());

    }

    @Test
    public void getLoginCommand() {
        when(request.getParameter("command")).thenReturn("login");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new LoginCommand().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());

    }

    @Test
    public void getRegistrationCommand() {
        when(request.getParameter("command")).thenReturn("registration");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new RegistrationCommand().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getLogoutCommand() {
        when(request.getParameter("command")).thenReturn("logout");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new LogoutCommand().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getAdmin() {
        when(request.getParameter("command")).thenReturn("admin");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new Admin().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getModeratorHome() {
        when(request.getParameter("command")).thenReturn("moderatorHome");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new ModeratorHome().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getAddingExpoCenter() {
        when(request.getParameter("command")).thenReturn("addExpoCenter");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new AddingExpoCenter().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getExpoCenterManagement() {
        when(request.getParameter("command")).thenReturn("expoCenterManagement");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new ExpoCenterManagement().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getEditCenter() {
        when(request.getParameter("command")).thenReturn("editExpositionCenter");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new EditCenter().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getAddExpo() {
        when(request.getParameter("command")).thenReturn("addExposition");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new AddExpo().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getExhibitionManagement() {
        when(request.getParameter("command")).thenReturn("expoManagement");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new ExhibitionManagement().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getEditExposition() {
        when(request.getParameter("command")).thenReturn("editExposition");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new EditExposition().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getCombineExWithExCenter() {
        when(request.getParameter("command"))
                .thenReturn("combineExpoWithCenter");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new CombineExWithExCenter().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getCreateContract() {
        when(request.getParameter("command"))
                .thenReturn("createContract");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new CreateContract().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getContractManagement() {
        when(request.getParameter("command"))
                .thenReturn("contractManagement");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new ContractManagement().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getEditContract() {
        when(request.getParameter("command"))
                .thenReturn("editContract");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new EditContract().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getPurchase() {
        when(request.getParameter("command"))
                .thenReturn("purchase");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new Purchase().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getExpoInfo() {
        when(request.getParameter("command"))
                .thenReturn("expoInfo");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new ExpoInfo().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getUserHome() {
        when(request.getParameter("command"))
                .thenReturn("userHome");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new UserHome().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getChangeLanguage() {
        when(request.getParameter("command"))
                .thenReturn("changeLang");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new ChangeLanguage().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getCheckOut() {
        when(request.getParameter("command"))
                .thenReturn("checkOut");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new CheckOut().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getPurchaseProcessing() {
        when(request.getParameter("command"))
                .thenReturn("purchaseProcessing");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new PurchaseProcessing().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getWaitApprovalTicket() {
        when(request.getParameter("command"))
                .thenReturn("waitApprovalTicket");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new WaitApprovalTicket().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

    @Test
    public void getApprovedTickets() {
        when(request.getParameter("command"))
                .thenReturn("approvedTicket");
        ControllerHelper controllerHelper = new ControllerHelper();

        assertEquals(new ApprovedTickets().getClass(),
                (controllerHelper.getCommand(request, response)).getClass());
    }

}