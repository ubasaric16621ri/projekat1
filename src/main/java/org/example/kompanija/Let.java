package org.example.kompanija;
import org.example.model.Polazak;
import java.time.LocalDateTime;
import java.util.*;

public class Let {
    public static List<Polazak> lista = new ArrayList<>();
    public static Map<String, Polazak> rezervacije = new HashMap<>();

    static {
        lista.add(new Polazak("JU240", "BEG", "CDG", LocalDateTime.of(2025, 7, 20, 7, 0), "AirSerbia", 10, 120, 240));
        lista.add(new Polazak("LH300", "BEG", "FRA", LocalDateTime.of(2025, 7, 20, 9, 0), "Lufthansa", 10, 100, 220));
        lista.add(new Polazak("KL100", "AMS", "BEG", LocalDateTime.of(2025, 7, 21, 15, 30), "KLM", 10, 80, 200));
        lista.add(new Polazak("JU240", "BEG", "CDG", LocalDateTime.of(2025,7,22,15,30),"AirSerbia" , 120, 70,250));
        lista.add(new Polazak("JU260", "BEG", "AMS", LocalDateTime.of(2025,7,22,15,30), "KLM", 120, 70,250));

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
