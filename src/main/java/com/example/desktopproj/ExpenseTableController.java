package com.example.desktopproj;

import com.example.desktopproj.classes.Expense;
import com.example.desktopproj.classes.ExpenseDAO;
import com.example.desktopproj.classes.LoggerUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.apache.log4j.Logger;

public class ExpenseTableController {
    private static final Logger logger = LoggerUtil.getLogger(ExpenseTableController.class);

    @FXML
    private TableView<Expense> tableView;

    @FXML
    public void initialize() {
        logger.info("Initialisation de la table des dépenses");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getItems().addAll(ExpenseDAO.getAll());
        logger.debug("Table initialisée avec " + tableView.getItems().size() + " lignes");
    }

    public void openAddRowModal(ActionEvent actionEvent) {
        logger.debug("Ouverture du dialogue d'ajout de dépense");
        AddExpenseDialog dialog = new AddExpenseDialog();
        dialog.showAndWait().ifPresent(expense -> {
            tableView.getItems().add(expense);
            if (ExpenseDAO.insert(expense)) {
                logger.info("Dépense ajoutée à la table et enregistrée en base de données");
            } else {
                logger.warn("La dépense a été ajoutée à la table mais pas enregistrée en base de données");
            }
        });
    }
}