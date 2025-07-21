package org.example.kompanija;

import org.example.model.*;

import java.rmi.RemoteException;
import java.util.List;

public class Obrada implements Rmi {

    @Override
    public Potvrda rezervisi(Rezervacija r) throws RemoteException {
        return Let.rezervisiLet(r);
    }

    @Override
    public Uplata plati(String oznakaRezervacije) throws RemoteException {
        return Let.platiLet(oznakaRezervacije);
    }

    @Override
    public List<Polazak> pretraziLetove(String sa, String ka, String datum) {
        return Let.lista;
    }
}
