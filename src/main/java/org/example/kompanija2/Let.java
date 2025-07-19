package org.example.kompanija2;
import org.example.model.Polazak;
import java.time.LocalDateTime;
import java.util.*;

public class Let {
    public static List<Polazak> lista = new ArrayList<>();
    public static Map<String, Polazak> rezervacije = new HashMap<>();

    static {
        lista.add(new Polazak("WZ500", "BEG", "CDG", LocalDateTime.of(2025, 7, 20, 7, 0), "WizzAir", 10, 90, 210));
        lista.add(new Polazak("AF111", "BEG", "CDG", LocalDateTime.of(2025, 7, 20, 9, 30), "AirFrance", 10, 130, 250));
    }

    public static Optional<Polazak> nadjiLet(String sa, String ka, String datum) {
        for (Polazak l : lista) {
            if (l.sa.equals(sa) && l.ka.equals(ka) && l.vreme.toLocalDate().toString().equals(datum) && l.slobodno > 0) {
                return Optional.of(l);
            }
        }
        return Optional.empty();
    }
}
