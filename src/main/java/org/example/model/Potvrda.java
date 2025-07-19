package org.example.model;

import java.io.Serializable;

public class Potvrda implements Serializable {
    public boolean uspesno;
    public String poruka;
    public String oznakaRezervacije;

    public Potvrda(boolean uspesno, String poruka, String oznakaRezervacije) {
        this.uspesno = uspesno;
        this.poruka = poruka;
        this.oznakaRezervacije = oznakaRezervacije;
    }
}
