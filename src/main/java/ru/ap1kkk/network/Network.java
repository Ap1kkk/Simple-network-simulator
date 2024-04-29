package ru.ap1kkk.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.ap1kkk.elements.ElementFactory;
import ru.ap1kkk.elements.LoadBalancer;
import ru.ap1kkk.elements.Producer;
import ru.ap1kkk.elements.Receiver;
import ru.ap1kkk.ports.Port;
import ru.ap1kkk.ports.PortFactory;
import ru.ap1kkk.serialization.InitValues;
import ru.ap1kkk.statistic.StatsMode;

import java.io.*;
import java.util.Collection;
import java.util.TimerTask;

@RequiredArgsConstructor
public class Network extends TimerTask {

//    private final int maxCycles;
    private final String initFile;
    private final PortFactory portFactory;
    private final ElementFactory elementFactory;
    private final static int DEFAULT_ITERATIONS = 10;
    @Getter
    private int iterations = 0;
    @Getter
    private StatsMode statsMode;
    private int cyclesCompleted = 0;

    @SneakyThrows
    public void initialize() {
        String jsonString = getJsonString();
        ObjectMapper objectMapper = new ObjectMapper();
        StringReader reader = new StringReader(jsonString.toString());

        InitValues initValues = objectMapper.readValue(reader, InitValues.class);

        statsMode = initValues.getStatsMode();
        if(initValues.getIterations() != null)
            iterations = initValues.getIterations();
        else
            iterations = DEFAULT_ITERATIONS;

        if(initValues.getLoadBalancers() != null) {
            for(LoadBalancer loadBalancer: initValues.getLoadBalancers()) {
                addPorts(loadBalancer.getDeliverPorts().values());
                addPorts(loadBalancer.getReceiverPorts().values());
                elementFactory.addLoadBalancer(loadBalancer);
            }
        }

        for(Producer producer: initValues.getProducers()) {
            addPorts(producer.getDeliverPorts().values());
            elementFactory.addProducer(producer);
        }

        for(Receiver receiver: initValues.getReceivers()) {
            addPorts(receiver.getReceiverPorts().values());
            elementFactory.addReceiver(receiver);
        }

        portFactory.init();

        initReceivers();
        validateElements();
    }

    private String getJsonString() {
        StringBuilder jsonString = new StringBuilder();

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(initFile)) {

            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader br = new BufferedReader(isr);) {
                String line;
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);
                }
                is.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonString.toString();
    }

    private void addPorts(Collection<Port> ports) {
        for(Port port: ports) {
            portFactory.addExistingPort(port);
        }
    }

    private void initReceivers() {
        for(Receiver receiver: ElementFactory.getReceiverPool().values()) {
            receiver.init();
        }
    }

    @Override
    public void run() {
        ++cyclesCompleted;
        System.out.println("Iteration: " + cyclesCompleted);

        transferData();
        earlyUpdate();
        update();
        process();
        resetPortValues();
    }

    private void transferData() {
        for(Producer producer: ElementFactory.getProducerPool().values()){
            producer.transferData();
        }
        for(LoadBalancer loadBalancer: ElementFactory.getLoadBalancerPool().values()) {
            loadBalancer.transferData();
        }
        for(Receiver receiver: ElementFactory.getReceiverPool().values()) {
            receiver.transferData();
        }
    }

    private void earlyUpdate() {
        for(Producer producer: ElementFactory.getProducerPool().values()){
            producer.earlyUpdate();
        }
        for(LoadBalancer loadBalancer: ElementFactory.getLoadBalancerPool().values()) {
            loadBalancer.earlyUpdate();
        }
        for(Receiver receiver: ElementFactory.getReceiverPool().values()) {
            receiver.earlyUpdate();
        }
    }

    private void update() {
        for(Producer producer: ElementFactory.getProducerPool().values()){
            producer.update();
        }
        for(LoadBalancer loadBalancer: ElementFactory.getLoadBalancerPool().values()) {
            loadBalancer.update();
        }
        for(Receiver receiver: ElementFactory.getReceiverPool().values()) {
            receiver.update();
        }
    }

    private void process() {
        for(Producer producer: ElementFactory.getProducerPool().values()){
            producer.process();
        }
        for(LoadBalancer loadBalancer: ElementFactory.getLoadBalancerPool().values()) {
            loadBalancer.process();
        }
        for(Receiver receiver: ElementFactory.getReceiverPool().values()) {
            receiver.process();
        }
    }

    private void resetPortValues() {
        for(Producer producer: ElementFactory.getProducerPool().values()){
            producer.resetPortValues();
        }
        for(LoadBalancer loadBalancer: ElementFactory.getLoadBalancerPool().values()) {
            loadBalancer.resetPortValues();
        }
        for(Receiver receiver: ElementFactory.getReceiverPool().values()) {
            receiver.resetPortValues();
        }
    }

    private void validateElements() {
        for(Producer producer: ElementFactory.getProducerPool().values()){
            producer.validate();
        }
        for(LoadBalancer loadBalancer: ElementFactory.getLoadBalancerPool().values()) {
            loadBalancer.validate();
        }
        for(Receiver receiver: ElementFactory.getReceiverPool().values()) {
            receiver.validate();
        }
    }
}
