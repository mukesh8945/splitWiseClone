package com.mukesh.Splitwise.controller.expense;

import com.mukesh.Splitwise.exception.expense.InvalidExpenseDetailsException;
import com.mukesh.Splitwise.model.Response;
import com.mukesh.Splitwise.model.expense.ExpenseUpdateRequest;
import com.mukesh.Splitwise.model.expense.GroupExpense;
import com.mukesh.Splitwise.service.expense.ExpenseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("splitwise/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/{groupId}")
    public ResponseEntity addExpense(@RequestBody GroupExpense groupExpense, @PathVariable UUID groupId) throws Exception {
        Response response = expenseService.addExpense(groupExpense, groupId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{groupId}/{expenseId}")
    public ResponseEntity deleteExpense(@PathVariable UUID groupId, @PathVariable UUID expenseId) throws Exception {
        Response response = expenseService.deleteExpense(groupId, expenseId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/{groupId}/{expenseId}")
    public ResponseEntity updateExpense(@PathVariable UUID groupId, @PathVariable UUID expenseId,
                                        @RequestBody ExpenseUpdateRequest updateRequest) throws Exception {
        Response response = expenseService.updateExpense(groupId, expenseId,updateRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
