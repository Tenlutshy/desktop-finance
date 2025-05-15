package com.example.desktopproj.classes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Expense {
    private LocalDate periode;
    private double total;
    private double logement;
    private double nourriture;
    private double sortie;
    private double transport;
    private double impot;
    private double autre;

    public Expense(LocalDate periode, double logement, double nourriture, double sortie, double transport, double impot, double autre) {
        this.periode = periode;
        this.total = total;
        this.logement = logement;
        this.nourriture = nourriture;
        this.sortie = sortie;
        this.transport = transport;
        this.impot = impot;
        this.autre = autre;
    }

    public Expense() {
        this(LocalDate.now(), 0d, 0d, 0d, 0d, 0d, 0d);
    }

    public String getPeriode() {
        return periode.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public void setPeriode(String periode) {
        this.periode = LocalDate.parse(periode, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }


    public double getTotal() {
        return this.autre + this.impot + this.transport + this.sortie + this.nourriture + this.logement;
    }

    public double getLogement() {
        return logement;
    }

    public void setLogement(double logement) {
        this.logement = logement;
    }

    public double getNourriture() {
        return nourriture;
    }

    public void setNourriture(double nourriture) {
        this.nourriture = nourriture;
    }

    public double getSortie() {
        return sortie;
    }

    public void setSortie(double sortie) {
        this.sortie = sortie;
    }

    public double getTransport() {
        return transport;
    }

    public void setTransport(double transport) {
        this.transport = transport;
    }

    public double getImpot() {
        return impot;
    }

    public void setImpot(double impot) {
        this.impot = impot;
    }

    public double getAutre() {
        return autre;
    }

    public void setAutre(double autre) {
        this.autre = autre;
    }
}
