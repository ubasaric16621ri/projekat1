package org.example.klijent;
import org.example.model.*;
import org.example.agent.Agent;
import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

public class Klijent {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        System.out.println("Izaberi nacin rezervacije: ");
        System.out.println("1. Rezervacija direktno preko kompanije");
        System.out.println("2. Rezervacija preko agenta");
        System.out.print("Izbor: ");
        int izbor = Integer.parseInt(scanner.nextLine());

        if (izbor == 1) {
            direktnaRezervacija();
        } else if (izbor == 2) {
            rezervacijaPrekoAgenta();
        } else {
            System.out.println("Nepostojeca opcija.");
        }
    }

    private static void direktnaRezervacija() throws Exception {
        System.out.println("Lista dostupnih kompanija");
        System.out.println("1. Air Serbia");
        System.out.println("2. Lufthansa");

        System.out.print("Izaberite kompaniju: ");
        int izbor = Integer.parseInt(scanner.nextLine());

        String rmiAdresa = null;
        if (izbor == 1) {
            rmiAdresa = "rmi://localhost:1099/prevoznik1";
        } else if (izbor == 2) {
            rmiAdresa = "rmi://localhost:1099/prevoznik2";
        } else {
            System.out.println("Nepoznata kompanija.");
            return;
        }

        Rmi rmi = (Rmi) Naming.lookup(rmiAdresa);
        List<Polazak> svi = rmi.pretraziLetove("", "", "");

        System.out.println("Lista letova za izabranu kompaniju");
        int i = 1;
        for (Polazak p : svi) {
            System.out.println(i++ + ". " + p.oznaka + " | " + p.sa + " -> " + p.ka + " | " + p.vreme + " | Slobodnih: " + p.slobodno + " | Cena: " + p.trenutnaCena());
        }

        System.out.print("Izaberite let po rednom broju: ");
        int broj = Integer.parseInt(scanner.nextLine());
        if (broj < 1 || broj > svi.size()) {
            System.out.println("Nepostojeci let.");
            return;
        }

        Polazak izabrani = svi.get(broj - 1);

        System.out.print("Unesite broj osoba: ");
        int brojOsoba = Integer.parseInt(scanner.nextLine());

        System.out.print("Da li zelite povratni let? (true/false): ");
        boolean povratni = Boolean.parseBoolean(scanner.nextLine());

        Rezervacija rez = new Rezervacija(izabrani.sa, izabrani.ka, izabrani.vreme.toLocalDate().toString(), brojOsoba, povratni);
        Potvrda potvrda = rmi.rezervisi(rez);
        System.out.println("Rezervacija ID: " + potvrda.oznakaRezervacije);

        System.out.print("Zelite li odmah da platite? (da/ne): ");
        if (scanner.nextLine().equalsIgnoreCase("da")) {
            Uplata uplata = rmi.plati(potvrda.oznakaRezervacije);
            if (uplata.prihvaceno) {
                System.out.println("Uplata uspesna! Cena: " + uplata.iznos* rez.brojOsoba);
            } else {
                System.out.println("Greska: " + uplata.poruka);
            }
        }
    }

    private static void rezervacijaPrekoAgenta() throws Exception {
        System.out.print("Unesite polazni grad: ");
        String sa = scanner.nextLine();

        System.out.print("Unesite dolazni grad: ");
        String ka = scanner.nextLine();

        System.out.print("Unesite datum (YYYY-MM-DD): ");
        String datum = scanner.nextLine();

        System.out.print("Unesite broj osoba: ");
        int brojOsoba = Integer.parseInt(scanner.nextLine());

        System.out.print("Da li zelite povratni let? (true/false): ");
        boolean povratni = Boolean.parseBoolean(scanner.nextLine());

        Polazak najboljiLet = Agent.pronadjiNajboljiLet(sa, ka, datum, brojOsoba);

        if (najboljiLet == null) {
            System.out.println("Nema dostupnih letova.");
            return;
        }

        System.out.println("Najpovoljniji let: " + najboljiLet.oznaka + " | " + najboljiLet.vreme + " | Cena: " + najboljiLet.trenutnaCena());

        System.out.print("Da li zelite da rezervisete? (da/ne): ");
        if (!scanner.nextLine().equalsIgnoreCase("da")) return;

        Rmi rmi = Agent.dobaviKompanijuZaLet(najboljiLet.oznaka);

        Rezervacija rez = new Rezervacija(najboljiLet.sa, najboljiLet.ka, najboljiLet.vreme.toLocalDate().toString(), brojOsoba, povratni);
        Potvrda potvrda = rmi.rezervisi(rez);
        System.out.println("Rezervacija ID: " + potvrda.oznakaRezervacije);

        System.out.print("Zelite li odmah da platite? (da/ne): ");
        if (scanner.nextLine().equalsIgnoreCase("da")) {
            Uplata uplata = rmi.plati(potvrda.oznakaRezervacije);
            if (uplata.prihvaceno) {
                System.out.println("Uplata uspesna! Cena: " + uplata.iznos * rez.brojOsoba);
            } else {
                System.out.println("Greska: " + uplata.poruka);
            }
        }
    }
}
