package com.example.desktopproj;

import com.example.desktopproj.classes.Expense;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.function.UnaryOperator;

class AddExpenseDialog extends Dialog<Expense> {
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
                    return controller.createExpenseFromInputs();
                }
                return null;
            });
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement de la boîte de dialogue", e);
        }
    }
}

public class AddRowModalController {
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
                date = LocalDate.now();
            }
            double logement = Double.parseDouble(logementField.getText());
            double nourriture = Double.parseDouble(nourritureField.getText());
            double sortie = Double.parseDouble(sortieField.getText());
            double transport = Double.parseDouble(transportField.getText());
            double impot = Double.parseDouble(impotField.getText());
            double autre = Double.parseDouble(autreField.getText());
            return new Expense(date, logement, nourriture, sortie, transport, impot, autre);

        } catch (Exception e) {
            System.err.println("Erreur lors de la création de la dépense: " + e.getMessage());
            return null;
        }

    }
}