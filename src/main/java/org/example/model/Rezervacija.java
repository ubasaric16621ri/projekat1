package org.example.model;

import java.io.Serializable;

public class Rezervacija implements Serializable {
    public String sa;
    public String ka;
    public String datum;
    public int brojOsoba;
    public boolean povratna;

    public Rezervacija(String sa, String ka, String datum, int brojOsoba, boolean povratna) {
        this.sa = sa;
        this.ka = ka;
        this.datum = datum;
        this.brojOsoba = brojOsoba;
        this.povratna = povratna;
    }
}
