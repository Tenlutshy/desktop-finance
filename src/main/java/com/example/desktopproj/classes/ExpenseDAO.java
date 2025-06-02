package com.example.desktopproj.classes;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExpenseDAO {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static boolean insert(Expense expense) {
        String sql = "INSERT INTO expense (date, logement, nourriture, sortie, transport, impot, autre) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, expense.getPeriode());
            pstmt.setDouble(2, expense.getLogement());
            pstmt.setDouble(3, expense.getNourriture());
            pstmt.setDouble(4, expense.getSortie());
            pstmt.setDouble(5, expense.getTransport());
            pstmt.setDouble(6, expense.getImpot());
            pstmt.setDouble(7, expense.getAutre());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Erreur lors de l'insertion d'une dépense: " + e.getMessage());
            return false;
        }
    }

    public static List<Expense> get12MonthsBefore(int month, int year) {
        // Crée la date du dernier jour du mois donné
        LocalDate endDate = LocalDate.of(year, month, 1).withDayOfMonth(1).plusMonths(1).minusDays(1);

        // Crée la date du premier jour 11 mois avant
        LocalDate startDate = endDate.minusMonths(11).withDayOfMonth(1);

        // Formater les dates en chaînes (dd/MM/yyyy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String startDateStr = startDate.format(formatter);
        String endDateStr = endDate.format(formatter);

        String sql = "SELECT date, logement, nourriture, sortie, transport, impot, autre FROM expense " +
                "WHERE date BETWEEN ? AND ? ORDER BY date";

        List<Expense> expenses = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, startDateStr);
            pstmt.setString(2, endDateStr);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String dateStr = rs.getString("date");
                    LocalDate date = LocalDate.parse(dateStr, formatter);

                    double logement = rs.getDouble("logement");
                    double nourriture = rs.getDouble("nourriture");
                    double sortie = rs.getDouble("sortie");
                    double transport = rs.getDouble("transport");
                    double impot = rs.getDouble("impot");
                    double autre = rs.getDouble("autre");

                    Expense expense = new Expense(date, logement, nourriture, sortie, transport, impot, autre);
                    expenses.add(expense);
                }
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    "Erreur lors de la récupération des dépenses de 12 mois avant " + month + "/" + year + ": " + e.getMessage()
            );
        }

        return expenses;
    }


    public static List<Expense> getByMonth(int month, int year) {
        // Formatage du pattern pour LIKE
        String datePattern = "__/" + String.format("%02d", month) + "/" + year;

        String sql = "SELECT date, logement, nourriture, sortie, transport, impot, autre FROM expense " +
                "WHERE date LIKE ?";

        List<Expense> expenses = new ArrayList<>();

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, datePattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String dateStr = rs.getString("date");
                    LocalDate date = LocalDate.parse(dateStr, FORMATTER);

                    double logement = rs.getDouble("logement");
                    double nourriture = rs.getDouble("nourriture");
                    double sortie = rs.getDouble("sortie");
                    double transport = rs.getDouble("transport");
                    double impot = rs.getDouble("impot");
                    double autre = rs.getDouble("autre");

                    Expense expense = new Expense(date, logement, nourriture, sortie, transport, impot, autre);
                    expenses.add(expense);
                }
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    "Erreur lors de la récupération des dépenses pour " + month + "/" + year + ": " + e.getMessage()
            );
        }

        return expenses;
    }


    public static List<Expense> getAll() {
        String sql = "SELECT date, logement, nourriture, sortie, transport, impot, autre FROM expense";
        List<Expense> expenses = new ArrayList<>();

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String dateStr = rs.getString("date");
                LocalDate date = LocalDate.parse(dateStr, FORMATTER);

                double logement = rs.getDouble("logement");
                double nourriture = rs.getDouble("nourriture");
                double sortie = rs.getDouble("sortie");
                double transport = rs.getDouble("transport");
                double impot = rs.getDouble("impot");
                double autre = rs.getDouble("autre");

                Expense expense = new Expense(date, logement, nourriture, sortie, transport, impot, autre);
                expenses.add(expense);
            }

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Erreur lors de la récupération des dépenses: " + e.getMessage());
        }

        return expenses;
    }

}
