package org.example.kompanija2;

import org.example.model.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Obrada implements Rmi {

    @Override
    public synchronized Potvrda rezervisi(Rezervacija r) throws RemoteException {
        Let.ocistiIstekleRezervacije();
        var trazeni = Let.nadjiLet(r.sa, r.ka, r.datum);
        if (trazeni.isEmpty()) {
            return new Potvrda(false, "Nema slobodnih letova.", null);
        }

        Polazak let = trazeni.get();
        if (let.slobodno < r.brojOsoba) {
            return new Potvrda(false, "Nema dovoljno mesta.", null);
        }

        let.slobodno -= r.brojOsoba;
        String sifra = UUID.randomUUID().toString().substring(0, 8);
        Let.rezervacije.put(sifra, new Let.RezInfo(let, r.brojOsoba, java.time.LocalDateTime.now()));
        System.out.println("Rezervisano " + r.brojOsoba + " mesta na letu " + let.oznaka);

        return new Potvrda(true, "Rezervacija uspesna.", sifra);
    }

    @Override
    public synchronized Uplata plati(String oznakaRezervacije) throws RemoteException {
        Let.ocistiIstekleRezervacije();
        Let.RezInfo info = Let.rezervacije.get(oznakaRezervacije);
        if (info == null) {
            return new Uplata(false, 0, "Rezervacija ne postoji ili je istekla.");
        }

        int cena = info.let.trenutnaCena();
        Let.rezervacije.remove(oznakaRezervacije);
        System.out.println("Placeno " + cena + " za let " + info.let.oznaka);
        return new Uplata(true, cena, "Placanje prihvaceno.");
    }

    public List<Polazak> pretraziLetove(String sa, String ka, String datum) {
        List<Polazak> rezultat = new ArrayList<>();
        for (Polazak let : Let.lista) {
            boolean okSa = sa.isEmpty() || let.sa.equalsIgnoreCase(sa);
            boolean okKa = ka.isEmpty() || let.ka.equalsIgnoreCase(ka);
            boolean okDatum = datum.isEmpty() || let.vreme.toLocalDate().toString().equals(datum);
            if (okSa && okKa && okDatum && let.slobodno > 0) {
                rezultat.add(let);
            }
        }
        return rezultat;
    }



}
