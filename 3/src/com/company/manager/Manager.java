package com.company.manager;

import java.rmi.RemoteException;

import com.company.shared.Beacon;

public interface Manager extends java.rmi.Remote {
    public int deposit(Beacon b) throws RemoteException;
}
