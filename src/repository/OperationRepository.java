package repository;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import interfaces.IR_OperationRepo;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OperationRepository implements IR_OperationRepo {
    private final AtomicInteger currentOperationId = new AtomicInteger(1);

    private final List<Operation> operations;


    public OperationRepository() {
        this.operations = new ArrayList<>();
    }

    @Override
    public Operation createOperation(User user, TypeOperation typeOperation, double amount, Currency currency, double rate) {
        return new Operation(currentOperationId.getAndIncrement(), user, typeOperation, amount, currency, rate);
    }

    @Override
    public void saveOperation(Operation operation) {
        operations.add(operation);
    }

    @Override
    public List<Operation> getUserOperations(User user) {
        return operations.stream().collect(Collectors.groupingBy(Operation::getUser)).getOrDefault(user, new ArrayList<>());
    }


}
