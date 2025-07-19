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
    /** Pocetni broj slobodnih mesta za obracun cene */
    public final int kapacitet;
    public int cena;
    public int maksCena;

    public Polazak(String oznaka, String sa, String ka, LocalDateTime vreme, String kompanija, int slobodno, int cena, int maksCena) {
        this.oznaka = oznaka;
        this.sa = sa;
        this.ka = ka;
        this.vreme = vreme;
        this.kompanija = kompanija;
        this.slobodno = slobodno;
        this.kapacitet = slobodno;
        this.cena = cena;
        this.maksCena = maksCena;
    }

    public synchronized int trenutnaCena() {
        int sold = kapacitet - slobodno;
        int korak = kapacitet > 0 ? sold / 5 : 0;
        int maxKorak = Math.max(1, kapacitet / 5);
        int increment = (maksCena - cena) / maxKorak;
        int novaCena = cena + korak * increment;
        if (novaCena > maksCena) novaCena = maksCena;
        return novaCena;
    }

    @Override
    public String toString() {
        return oznaka + ": " + sa + " -> " + ka + ", " + kompanija + ", " + vreme.toString();
    }
}
