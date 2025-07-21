package org.example.kompanija;

import org.example.model.*;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Obrada implements Rmi {

    @Override
    public Potvrda rezervisi(Rezervacija r) throws RemoteException {
        Let.ocistiIstekleRezervacije();
        Optional<Polazak> trazeni = Let.nadjiLet(r.sa, r.ka, r.datum);
        if (trazeni.isEmpty()) {
            return new Potvrda(false, "Nema slobodnih letova.", null);
        }

        Polazak let = trazeni.get();

        // Model-level synchronizacija
        if (!let.pokusajRezervaciju(r.brojOsoba)) {
            return new Potvrda(false, "Nema dovoljno mesta.", null);
        }

        String sifra = UUID.randomUUID().toString().substring(0, 8);
        synchronized (Let.rezervacije) {
            Let.rezervacije.put(sifra, new Let.RezInfo(let, r.brojOsoba, LocalDateTime.now()));
        }

        System.out.println("Rezervisano " + r.brojOsoba + " mesta na letu " + let.oznaka);
        return new Potvrda(true, "Rezervacija uspesna.", sifra);
    }

    @Override
    public Uplata plati(String oznakaRezervacije) throws RemoteException {
        Let.ocistiIstekleRezervacije();
        Let.RezInfo info;

        synchronized (Let.rezervacije) {
            info = Let.rezervacije.remove(oznakaRezervacije);
        }

        if (info == null) {
            return new Uplata(false, 0, "Rezervacija ne postoji ili je istekla.");
        }

        int cena = info.let.trenutnaCena();

        synchronized (Let.dnevnaZarada) {
            Let.dnevnaZarada.merge(LocalDate.now(), cena, Integer::sum);
        }

        System.out.println("Placeno " + cena + " za let " + info.let.oznaka);
        return new Uplata(true, cena, "Placanje prihvaceno.");
    }

    @Override
    public List<Polazak> pretraziLetove(String sa, String ka, String datum) {
        List<Polazak> rezultat = new ArrayList<>();
        for (Polazak let : Let.lista) {
            boolean okSa = sa.isEmpty() || let.sa.equalsIgnoreCase(sa);
            boolean okKa = ka.isEmpty() || let.ka.equalsIgnoreCase(ka);
            boolean okDatum = datum.isEmpty() || let.vreme.toLocalDate().toString().equals(datum);
            if (okSa && okKa && okDatum && let.getSlobodno() > 0) {
                rezultat.add(let);
            }
        }
        return rezultat;
    }
}
