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
