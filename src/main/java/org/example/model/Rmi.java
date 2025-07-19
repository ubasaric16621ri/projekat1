package org.example.model;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Rmi extends Remote {
    Potvrda rezervisi(Rezervacija r) throws RemoteException;
    Uplata plati(String oznakaRezervacije) throws RemoteException;
    List<Polazak> pretraziLetove(String sa, String ka, String datum) throws RemoteException;

}
