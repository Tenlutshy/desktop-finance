package com.example.desktopproj;

import com.example.desktopproj.classes.Expense;
import com.example.desktopproj.classes.ExpenseDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardController {

    private final List<Expense> expensesMonth = new ArrayList<>();
    private final List<Expense> expensesYear = new ArrayList<>();
    private int currentMonth;
    private int currentYear;


    @FXML
    private PieChart pieChart;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private ComboBox<String> monthSelector;

    @FXML
    private Label noDataLabel;


    @FXML
    public void initialize() {
        LocalDate now = LocalDate.now();
        currentMonth = now.getMonthValue();
        currentYear = now.getYear();

        setupMonthSelector();
        updateCharts();
        updatePieChart();
    }

    private void setupMonthSelector() {
        List<String> monthNames = new ArrayList<>();
        Locale locale = Locale.FRENCH;

        for (int i = 1; i <= 12; i++) {
            String monthName = Month.of(i).getDisplayName(TextStyle.FULL, locale);
            monthNames.add(monthName);
        }

        monthSelector.setItems(FXCollections.observableArrayList(monthNames));
        monthSelector.getSelectionModel().select(currentMonth - 1);

        monthSelector.setOnAction(event -> {
            currentMonth = monthSelector.getSelectionModel().getSelectedIndex() + 1;
            updateCharts();
            updatePieChart();
        });
    }

    private void updateCharts() {
        pieChart.getData().clear();
        expensesMonth.clear();


        expensesMonth.addAll(ExpenseDAO.getByMonth(currentMonth, currentYear));

        double logementSum = 0;
        double nourritureSum = 0;
        double sortieSum = 0;
        double transportSum = 0;
        double impotSum = 0;
        double autreSum = 0;

        for (Expense expense : expensesMonth) {
            logementSum += expense.getLogement();
            nourritureSum += expense.getNourriture();
            sortieSum += expense.getSortie();
            transportSum += expense.getTransport();
            impotSum += expense.getImpot();
            autreSum += expense.getAutre();
        }

        boolean hasData = expensesMonth.size() > 0 &&
                (logementSum + nourritureSum + sortieSum + transportSum + impotSum + autreSum > 0);

        if (hasData) {
            // Afficher le camembert et masquer le message
            pieChart.setVisible(true);
            noDataLabel.setVisible(false);

            pieChart.getData().addAll(
                    new PieChart.Data("Logement", logementSum),
                    new PieChart.Data("Nourriture", nourritureSum),
                    new PieChart.Data("Sortie", sortieSum),
                    new PieChart.Data("Transport", transportSum),
                    new PieChart.Data("Impôt", impotSum),
                    new PieChart.Data("Autres", autreSum)
            );

            pieChart.setTitle("Répartition des dépenses - " +
                    Month.of(currentMonth).getDisplayName(TextStyle.FULL, Locale.FRENCH) +
                    " " + currentYear);
        } else {
            pieChart.setVisible(false);
            noDataLabel.setVisible(true);

            String moisCourant = Month.of(currentMonth).getDisplayName(TextStyle.FULL, Locale.FRENCH);
            noDataLabel.setText("Aucune donnée disponible pour " + moisCourant + " " + currentYear);
        }

    }

    private void updatePieChart() {
        lineChart.getData().clear();
        expensesYear.clear();

        YearMonth selectedYearMonth = YearMonth.of(currentYear, currentMonth);

        // Générer les 12 mois à afficher (11 mois précédents + le mois sélectionné)
        List<YearMonth> yearMonths = new ArrayList<>();
        for (int i = 11; i >= 0; i--) {
            yearMonths.add(selectedYearMonth.minusMonths(i));
        }

        // Générer les labels pour les mois
        List<String> labelsMois = new ArrayList<>();
        for (YearMonth ym : yearMonths) {
            String shortMonthName = ym.getMonth().getDisplayName(TextStyle.SHORT, Locale.FRENCH) + ym.getYear();

            labelsMois.add(shortMonthName);
        }

        // Charger les données des 12 derniers mois
        expensesYear.addAll(ExpenseDAO.get12MonthsBefore(currentMonth, currentYear));

        // Initialiser les tableaux pour stocker les totaux
        double[] totauxLogement = new double[12];
        double[] totauxNourriture = new double[12];
        double[] totauxTransport = new double[12];
        double[] totauxSortie = new double[12];
        double[] totauxImpot = new double[12];
        double[] totauxAutre = new double[12];
        double[] totauxTotaux = new double[12];

        for (int i = 0; i < 12; i++) {
            totauxLogement[i] = 0.0;
            totauxNourriture[i] = 0.0;
            totauxTransport[i] = 0.0;
            totauxSortie[i] = 0.0;
            totauxImpot[i] = 0.0;
            totauxAutre[i] = 0.0;
            totauxTotaux[i] = 0.0;
        }

        // Associer chaque dépense à son index dans les tableaux
        for (Expense expense : expensesYear) {
            YearMonth expenseYearMonth = YearMonth.from(expense.getPeriodeAsLocalDate());

            // Trouver l'index du mois dans notre liste de YearMonth
            int index = yearMonths.indexOf(expenseYearMonth);

            // Si le mois est dans notre plage de 12 mois
            if (index != -1) {
                totauxLogement[index] += expense.getLogement();
                totauxNourriture[index] += expense.getNourriture();
                totauxTransport[index] += expense.getTransport();
                totauxSortie[index] += expense.getSortie();
                totauxImpot[index] += expense.getImpot();
                totauxAutre[index] += expense.getAutre();
                totauxTotaux[index] += expense.getTotal();
            }
        }

        // Créer les séries pour le graphique
        XYChart.Series<String, Number> serieLogement = new XYChart.Series<>();
        serieLogement.setName("Logement");

        XYChart.Series<String, Number> serieNourriture = new XYChart.Series<>();
        serieNourriture.setName("Nourriture");

        XYChart.Series<String, Number> serieSortie = new XYChart.Series<>();
        serieSortie.setName("Sortie");

        XYChart.Series<String, Number> serieTransport = new XYChart.Series<>();
        serieTransport.setName("Transport");

        XYChart.Series<String, Number> serieImpot = new XYChart.Series<>();
        serieImpot.setName("Impôt");

        XYChart.Series<String, Number> serieAutre = new XYChart.Series<>();
        serieAutre.setName("Autre");

        XYChart.Series<String, Number> serieTotal = new XYChart.Series<>();
        serieTotal.setName("Total");

        for (int i = 0; i < 12; i++) {
            serieLogement.getData().add(new XYChart.Data<>(labelsMois.get(i), totauxLogement[i]));
            serieNourriture.getData().add(new XYChart.Data<>(labelsMois.get(i), totauxNourriture[i]));
            serieSortie.getData().add(new XYChart.Data<>(labelsMois.get(i), totauxSortie[i]));
            serieTransport.getData().add(new XYChart.Data<>(labelsMois.get(i), totauxTransport[i]));
            serieImpot.getData().add(new XYChart.Data<>(labelsMois.get(i), totauxImpot[i]));
            serieAutre.getData().add(new XYChart.Data<>(labelsMois.get(i), totauxAutre[i]));
            serieTotal.getData().add(new XYChart.Data<>(labelsMois.get(i), totauxTotaux[i]));
        }

        lineChart.getData().addAll(
                serieLogement,
                serieNourriture,
                serieSortie,
                serieTransport,
                serieImpot,
                serieAutre,
                serieTotal
        );

        YearMonth fromYM = yearMonths.get(0);
        YearMonth toYM = yearMonths.get(11);

        String fromDate = fromYM.getMonth().getDisplayName(TextStyle.SHORT, Locale.FRENCH) + " " + fromYM.getYear();
        String toDate = toYM.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH) + " " + toYM.getYear();

        lineChart.setTitle("Évolution des dépenses de " + fromDate + " à " + toDate);
    }


}
