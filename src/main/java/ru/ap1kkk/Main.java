package ru.ap1kkk;

import ru.ap1kkk.elements.ElementFactory;
import ru.ap1kkk.network.Network;
import ru.ap1kkk.ports.Port;
import ru.ap1kkk.ports.PortFactory;
import ru.ap1kkk.ports.PortType;
import ru.ap1kkk.statistic.StatisticCollector;
import ru.ap1kkk.statistic.StatsMode;

import java.util.Scanner;
import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter init filename: ");
        String filename = scanner.nextLine();

        System.out.println("Entered filename: " + filename);

        if(filename.isBlank())
            filename = "init.json";

        try {
            PortFactory portFactory = new PortFactory();
            ElementFactory elementFactory = new ElementFactory();

            Network network = new Network(filename, portFactory, elementFactory);

            network.initialize();
            for (int i = 0; i < network.getIterations(); i++) {
                network.run();
            }

            StatisticCollector.printStats(network.getStatsMode());
        } catch (Exception e) {
            System.out.println("Program aborted due to: " + e.getMessage());
            System.exit(-1);
        }
    }
}