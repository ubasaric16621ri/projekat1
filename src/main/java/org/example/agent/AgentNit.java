    package org.example.agent;

    import org.example.model.Polazak;
    import org.example.model.Rmi;

    import java.rmi.Naming;
    import java.util.List;
    import java.util.concurrent.Callable;

    public class AgentNit implements Callable<Polazak> {

        private final String adresa;
        private final String sa, ka, datum;

        public AgentNit(String adresa, String sa, String ka, String datum) {
            this.adresa = adresa;
            this.sa = sa;
            this.ka = ka;
            this.datum = datum;
        }

        @Override
        public Polazak call() throws Exception {
            Rmi rmi = (Rmi) Naming.lookup(adresa);
            List<Polazak> lista = rmi.pretraziLetove(sa, ka, datum);

            Polazak najjeftiniji = null;
            int najnizaCena = Integer.MAX_VALUE;

            for (Polazak p : lista) {
                int cena = p.trenutnaCena();
                if (p.slobodno > 0 && cena < najnizaCena) {
                    najnizaCena = cena;
                    najjeftiniji = p;
                }
            }

            return najjeftiniji;
        }
    }
