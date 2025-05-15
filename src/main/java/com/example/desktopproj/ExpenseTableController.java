package com.example.desktopproj;

import com.example.desktopproj.classes.Expense;
import com.example.desktopproj.classes.ExpenseDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class ExpenseTableController {
    @FXML
    private TableView<Expense> tableView;

    @FXML
    public void initialize() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getItems().addAll(ExpenseDAO.getAll());
    }

    public void openAddRowModal(ActionEvent actionEvent) {
        AddExpenseDialog dialog = new AddExpenseDialog();
        dialog.showAndWait().ifPresent(expense -> {
            tableView.getItems().add(expense);
            ExpenseDAO.insert(expense);
        });

    }
}
