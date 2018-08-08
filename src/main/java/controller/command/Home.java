package controller.command;

import dao.Connection.ConnectionPoolMySql;
import dao.mysql.ExhibitionContractMySql;
import dao.mysql.FactoryMySql;
import entities.Contract;
import entities.Exhibition;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Home implements Command {
    List<Contract> contracts;
    List<TestCl> listM;


    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getExpoInfoFromDB();
//        req.setAttribute("contracts", contracts);
        req.setAttribute("listM", listM);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/views/home.jsp");
        dispatcher.forward(req, resp);
    }

    // test code
    void getExpoInfoFromDB() {
        try {
            Connection connection = ConnectionPoolMySql.getInstance().getConnection();
            FactoryMySql factoryMySql = new FactoryMySql();
            ExhibitionContractMySql exhibitionContractMySql
                    = (ExhibitionContractMySql) factoryMySql.createExhibitionContract(connection);
//
//            Contract contract = new Contract();
//            contract.setExhibitionId(222);
//
//            contracts.add(contract);
//            contracts.add(contract);
//            contracts.add(contract);
//           contracts = exhibitionContractMySql.getAllContracts();

            listM = new ArrayList<>();
            listM.add(new TestCl("2", "dwa"));
            listM.add(new TestCl("3", "tri"));
            listM.add(new TestCl("4", "chotiri"));
            listM.add(new TestCl("5", "5"));
            listM.add(new TestCl("6", "6"));
         } catch (Exception exception) {

        }
    }


}


