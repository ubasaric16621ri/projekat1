package org.example.kompanija2;

import org.example.model.*;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.example.kompanija2.Let.lista;

public class Obrada implements Rmi {

    @Override
    public synchronized Potvrda rezervisi(Rezervacija r) throws RemoteException {
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
        Let.rezervacije.put(sifra, let);
        System.out.println("[PREVOZNIK] Rezervisano " + r.brojOsoba + " mesta na letu " + let.oznaka);

        return new Potvrda(true, "Rezervacija uspesna.", sifra);
    }

    @Override
    public Uplata plati(String oznakaRezervacije) throws RemoteException {
        Polazak let = Let.rezervacije.get(oznakaRezervacije);
        if (let == null) {
            return new Uplata(false, 0, "Rezervacija ne postoji ili je istekla.");
        }

        int cena = let.trenutnaCena();
        Let.rezervacije.remove(oznakaRezervacije);
        System.out.println("[PREVOZNIK] Placeno " + cena + " za let " + let.oznaka);
        return new Uplata(true, cena, "Placanje prihvaceno.");
    }

    public List<Polazak> pretraziLetove(String sa, String ka, String datum) {
        List<Polazak> rezultat = new ArrayList<>();
        for (Polazak let : Let.lista) {
            if (let.sa.equalsIgnoreCase(sa)
                    && let.ka.equalsIgnoreCase(ka)
                    && let.vreme.toLocalDate().toString().equals(datum)
                    && let.slobodno > 0) {
                rezultat.add(let);
            }
        }
        return rezultat;
    }



}
