package org.example.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Polazak implements Serializable {
    public String oznaka;
    public String sa;
    public String ka;
    public LocalDateTime vreme;
    public String kompanija;
    public int slobodno;
    public int cena;
    public int maksCena;

    public Polazak(String oznaka, String sa, String ka, LocalDateTime vreme, String kompanija, int slobodno, int cena, int maksCena) {
        this.oznaka = oznaka;
        this.sa = sa;
        this.ka = ka;
        this.vreme = vreme;
        this.kompanija = kompanija;
        this.slobodno = slobodno;
        this.cena = cena;
        this.maksCena = maksCena;
    }

    public int trenutnaCena() {
        int razlika = maksCena - cena;
        int korak = (10 - slobodno) / 5;
        return cena + korak * (razlika / 3);
    }

    @Override
    public String toString() {
        return oznaka + ": " + sa + " -> " + ka + ", " + kompanija + ", " + vreme.toString();
    }
}
