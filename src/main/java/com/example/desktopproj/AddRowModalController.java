package com.example.desktopproj;

import com.example.desktopproj.classes.Expense;
import com.example.desktopproj.classes.LoggerUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.function.UnaryOperator;

class AddExpenseDialog extends Dialog<Expense> {
    private static final Logger logger = LoggerUtil.getLogger(AddExpenseDialog.class);

    public AddExpenseDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-row-modal.fxml"));
            DialogPane dialogPane = loader.load();

            AddRowModalController controller = loader.getController();

            setDialogPane(dialogPane);
            setTitle("Ajouter une nouvelle dépense");

            getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            setResultConverter(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    logger.debug("Bouton OK cliqué, création de la dépense");
                    return controller.createExpenseFromInputs();
                }
                logger.debug("Dialogue annulé");
                return null;
            });
        } catch (IOException e) {
            logger.error("Erreur lors du chargement de la boîte de dialogue: " + e.getMessage());
            throw new RuntimeException("Erreur lors du chargement de la boîte de dialogue", e);
        }
    }
}

public class AddRowModalController {
    private static final Logger logger = LoggerUtil.getLogger(AddRowModalController.class);

    UnaryOperator<TextFormatter.Change> numberValidator = text -> {
        if (text.isReplaced()) {
            if (text.getText().matches("[^0-9]")) {
                text.setText(text.getControlText().substring(text.getRangeStart(), text.getRangeEnd()));
            }
        }

        if (text.isAdded()) {
            if (text.getControlText().contains(".")) {
                if (text.getText().matches("[0-9]")) {
                    text.setText("");
                }
            } else if (text.getText().matches("[^0-9.]")) {
                text.setText("");
            }
        }
        return text;
    };
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField logementField;
    @FXML
    private TextField nourritureField;
    @FXML
    private TextField sortieField;
    @FXML
    private TextField transportField;
    @FXML
    private TextField impotField;
    @FXML
    private TextField autreField;

    @FXML
    public void initialize() {
        logger.debug("Initialisation du formulaire d'ajout de dépense");
        datePicker.setValue(LocalDate.now());
        logementField.setTextFormatter(new TextFormatter<>(numberValidator));
        logementField.setText("0");

        nourritureField.setTextFormatter(new TextFormatter<>(numberValidator));
        nourritureField.setText("0");

        sortieField.setTextFormatter(new TextFormatter<>(numberValidator));
        sortieField.setText("0");

        transportField.setTextFormatter(new TextFormatter<>(numberValidator));
        transportField.setText("0");

        impotField.setTextFormatter(new TextFormatter<>(numberValidator));
        impotField.setText("0");

        autreField.setTextFormatter(new TextFormatter<>(numberValidator));
        autreField.setText("0");
    }

    public Expense createExpenseFromInputs() {
        try {
            LocalDate date = datePicker.getValue();
            if (date == null) {
                logger.warn("Aucune date sélectionnée, utilisation de la date actuelle");
                date = LocalDate.now();
            }
            double logement = Double.parseDouble(logementField.getText());
            double nourriture = Double.parseDouble(nourritureField.getText());
            double sortie = Double.parseDouble(sortieField.getText());
            double transport = Double.parseDouble(transportField.getText());
            double impot = Double.parseDouble(impotField.getText());
            double autre = Double.parseDouble(autreField.getText());

            Expense expense = new Expense(date, logement, nourriture, sortie, transport, impot, autre);
            logger.info("Nouvelle dépense créée pour la date " + expense.getPeriode());
            return expense;

        } catch (Exception e) {
            logger.error("Erreur lors de la création de la dépense: " + e.getMessage());
            return null;
        }
    }
}