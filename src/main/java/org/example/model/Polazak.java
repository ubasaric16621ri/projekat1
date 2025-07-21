package org.example.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Polazak implements Serializable {
    public final String oznaka;
    public final String sa;
    public final String ka;
    public final LocalDateTime vreme;
    public final String kompanija;
    private int slobodno;
    public final int kapacitet;
    public final int cena;
    public final int maksCena;

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

    public synchronized boolean pokusajRezervaciju(int brojMesta) {
        if (slobodno >= brojMesta) {
            slobodno -= brojMesta;
            return true;
        }
        return false;
    }

    public synchronized int getSlobodno() {
        return slobodno;
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
