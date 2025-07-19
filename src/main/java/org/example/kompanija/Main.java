package org.example.kompanija;
import org.example.model.Rmi;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    public static void main(String[] args) {
        try {
            // Pravimo instancu klase Obrada i pravimo RMI stub
            Rmi stub = (Rmi) UnicastRemoteObject.exportObject(new Obrada(), 0);

            // Pokrećemo RMI registry na portu 1099
            Registry reg = LocateRegistry.createRegistry(1099);

            // Registrujemo stub pod imenom 'prevoznik1'
            reg.rebind("prevoznik1", stub);

            System.out.println("[PREVOZNIK] Pokrenut kao 'prevoznik1'.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
