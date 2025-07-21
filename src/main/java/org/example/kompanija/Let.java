package org.example.kompanija;

import org.example.model.Polazak;
import org.example.model.Potvrda;
import org.example.model.Rezervacija;
import org.example.model.Uplata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Let {

    public static List<Polazak> lista = new ArrayList<>();
    public static Map<String, RezInfo> rezervacije = new HashMap<>();
    public static Map<LocalDate, Integer> dnevnaZarada = new HashMap<>();

    static {
        lista.add(new Polazak("JU240", "BEG", "CDG", LocalDateTime.of(2025, 7, 20, 7, 0), "AirSerbia", 10, 120, 240));
        lista.add(new Polazak("LH300", "BEG", "FRA", LocalDateTime.of(2025, 7, 20, 9, 0), "AirSerbia", 10, 100, 220));
        lista.add(new Polazak("KL100", "AMS", "BEG", LocalDateTime.of(2025, 7, 21, 15, 30), "AirSerbia", 10, 80, 200));
        lista.add(new Polazak("JU240", "BEG", "CDG", LocalDateTime.of(2025, 7, 22, 15, 30), "AirSerbia", 120, 70, 250));
        lista.add(new Polazak("JU260", "BEG", "AMS", LocalDateTime.of(2025, 7, 22, 15, 30), "AirSerbia", 120, 70, 250));
    }

    public static synchronized Potvrda rezervisiLet(Rezervacija r) {
        ocistiIstekleRezervacije();
        Optional<Polazak> trazeni = nadjiLet(r.sa, r.ka, r.datum);
        if (trazeni.isEmpty()) {
            return new Potvrda(false, "Nema slobodnih letova.", null);
        }

        Polazak let = trazeni.get();
        if (!let.pokusajRezervaciju(r.brojOsoba)) {
            return new Potvrda(false, "Nema dovoljno mesta.", null);
        }

        String sifra = UUID.randomUUID().toString().substring(0, 8);
        rezervacije.put(sifra, new RezInfo(let, r.brojOsoba, LocalDateTime.now()));

        System.out.println("Rezervisano " + r.brojOsoba + " mesta na letu " + let.oznaka);
        return new Potvrda(true, "Rezervacija uspesna.", sifra);
    }

    public static synchronized Uplata platiLet(String oznakaRezervacije) {
        ocistiIstekleRezervacije();
        RezInfo info = rezervacije.remove(oznakaRezervacije);
        if (info == null) {
            return new Uplata(false, 0, "Rezervacija ne postoji ili je istekla.");
        }

        int cena = info.let.trenutnaCena();
        dnevnaZarada.merge(LocalDate.now(), cena, Integer::sum);

        System.out.println("Placeno " + cena + " za let " + info.let.oznaka);
        return new Uplata(true, cena, "Placanje prihvaceno.");
    }

    public static Optional<Polazak> nadjiLet(String sa, String ka, String datum) {
        ocistiIstekleRezervacije();
        for (Polazak l : lista) {
            if (l.sa.equals(sa) && l.ka.equals(ka) && l.vreme.toLocalDate().toString().equals(datum) && l.getSlobodno() > 0) {
                return Optional.of(l);
            }
        }
        return Optional.empty();
    }

    public static synchronized void ocistiIstekleRezervacije() {
        Iterator<Map.Entry<String, RezInfo>> it = rezervacije.entrySet().iterator();
        LocalDateTime sada = LocalDateTime.now();
        while (it.hasNext()) {
            var e = it.next();
            if (e.getValue().vreme.plusDays(1).isBefore(sada)) {
                e.getValue().let.vratiMesta(e.getValue().broj);
                it.remove();
            }
        }
    }

    public static class RezInfo {
        public Polazak let;
        public int broj;
        public LocalDateTime vreme;

        public RezInfo(Polazak let, int broj, LocalDateTime vreme) {
            this.let = let;
            this.broj = broj;
            this.vreme = vreme;
        }
    }
}
