package org.example.agent;
import org.example.model.Polazak;
import org.example.model.Rmi;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Agent {

    private static final ExecutorService executor = Executors.newFixedThreadPool(3);

    public static Polazak pronadjiNajboljiLet(String sa, String ka, String datum, int brojOsoba) throws Exception {
        List<Future<Polazak>> rezultati = new ArrayList<>();
        rezultati.add(executor.submit(new AgentNit("rmi://localhost:1099/prevoznik1", sa, ka, datum)));
        rezultati.add(executor.submit(new AgentNit("rmi://localhost:1099/prevoznik2", sa, ka, datum)));

        Polazak najboljiLet = null;
        int najboljaCena = Integer.MAX_VALUE;

        for (Future<Polazak> f : rezultati) {
            Polazak p = f.get();
            if (p != null && p.slobodno >= brojOsoba && p.trenutnaCena() < najboljaCena) {
                najboljaCena = p.trenutnaCena();
                najboljiLet = p;
            }
        }

        return najboljiLet;
    }

    public static Rmi dobaviKompanijuZaLet(String oznaka) throws Exception {
        if (oznaka.startsWith("LFT") ) {
            return (Rmi) Naming.lookup("rmi://localhost:1099/prevoznik2");
        }
        return (Rmi) Naming.lookup("rmi://localhost:1099/prevoznik1");
    }
}
