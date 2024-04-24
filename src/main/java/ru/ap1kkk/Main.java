package ru.ap1kkk;

import ru.ap1kkk.elements.ElementFactory;
import ru.ap1kkk.network.Network;
import ru.ap1kkk.ports.Port;
import ru.ap1kkk.ports.PortFactory;
import ru.ap1kkk.ports.PortType;

import java.util.Timer;

public class Main {
    private static final int ITERATIONS = 2;

    public static void main(String[] args) {
        PortFactory portFactory = new PortFactory();
        ElementFactory elementFactory = new ElementFactory();

        Network network = new Network(portFactory, elementFactory);
        network.initialize();
//        Timer timer = new Timer();
//        timer.schedule(network, 100, 5000);
        for (int i = 0; i < ITERATIONS; i++) {
            network.run();
        }
    }
}