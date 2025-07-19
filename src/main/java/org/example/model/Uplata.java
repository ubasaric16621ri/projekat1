package org.example.model;

import java.io.Serializable;

public class Uplata implements Serializable {
    public boolean prihvaceno;
    public int iznos;
    public String poruka;

    public Uplata(boolean prihvaceno, int iznos, String poruka) {
        this.prihvaceno = prihvaceno;
        this.iznos = iznos;
        this.poruka = poruka;
    }
}
