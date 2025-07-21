package org.example.kompanija;
import org.example.model.Rmi;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    public static void main(String[] args) {
        try {
            Rmi stub = (Rmi) UnicastRemoteObject.exportObject(new Obrada(), 0);
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("prevoznik1", stub);

            System.out.println("Pokrenut kao 'prevoznik1'.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
