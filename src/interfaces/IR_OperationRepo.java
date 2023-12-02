package interfaces;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import model.*;

import java.util.List;
import java.util.Map;

public interface IR_OperationRepo {

    Operation createOperation(User user, TypeOperation typeOperation, double amountSell, Currency currencySell, double rateSell);

    void saveOperation(Operation operation);

    //TODO new
    List<Operation> getUserOperations(User user);



    }
