package org.example.kompanija2;

import org.example.kompanija2.Obrada;
import org.example.model.Rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    public static void main(String[] args) {
        try {
            Rmi stub = (Rmi) UnicastRemoteObject.exportObject(new Obrada(), 0);
            Registry reg = LocateRegistry.getRegistry(1099);
            reg.rebind("prevoznik2", stub);
            System.out.println("[PREVOZNIK2] Spreman kao 'prevoznik2'.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
