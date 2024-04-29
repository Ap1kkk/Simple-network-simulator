package ru.ap1kkk.elements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.SneakyThrows;
import ru.ap1kkk.ports.Metadata;
import ru.ap1kkk.ports.Port;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

@JsonAutoDetect
public class LoadBalancer extends Element {
    private int lastDeliveredPortIndex = -1;
    private int portsCollectedValue = 0;
    @JsonProperty("algorithm")
    private AlgorithmType algorithm;

    private LoadBalancer() {
        super(null,null, null);
    }
    public LoadBalancer(
            int id,
            HashMap<Integer, Port> receiverPorts,
            HashMap<Integer, Port> deliverPorts
    ) {
        super(id, receiverPorts, deliverPorts);
    }

    @Override
    public void transferData() {
    }

    @Override
    public void earlyUpdate() {
        if(algorithm.equals(AlgorithmType.WRR))
            weightedRoundRobin();
        else
            roundRobin();
    }

    @Override
    public void update() {
    }

    @Override
    public void process() {
    }

    @Override
    @SneakyThrows
    public void validate() {
        if(getReceiverPorts() == null || getReceiverPorts().isEmpty()
                || getDeliverPorts() == null || getDeliverPorts().isEmpty())
            throw new Exception(String.format("LoadBalancer %s: receiver and deliver ports must not be null", getId()));
    }

    private int calculatePortWeight(Port port) {
        Metadata metadata = port.getConnectedPort().getMetadata();
        int maxLoad = metadata.elementMaxLoad();
        int currentLoad = metadata.elementCurrentLoad();
        int processRate = metadata.elementProcessRate();

        if(currentLoad <= maxLoad)
            return processRate + (maxLoad - currentLoad);
        else
            return processRate;
    }

    private void collectPortsValue() {
        portsCollectedValue = 0;
        for (Port port: getReceiverPorts().values()) {
            portsCollectedValue += port.getReceivedValue();
        }
    }

    private void roundRobin() {
        collectPortsValue();

        Collection<Port> deliverPorts = getDeliverPorts().values();

        if (deliverPorts.isEmpty()) {
            return;
        }

        lastDeliveredPortIndex++;
        if (lastDeliveredPortIndex >= deliverPorts.size()) {
            lastDeliveredPortIndex = 0;
        }

        deliverPorts.stream()
                .skip(lastDeliveredPortIndex)
                .findFirst()
                .ifPresent(
                        nextPort -> nextPort.deliver(portsCollectedValue)
                );
    }

    private void weightedRoundRobin() {
        collectPortsValue();

        // Если нет портов доставки, выходим из метода
        if (getDeliverPorts().isEmpty()) {
            return;
        }
        Collection<Port> deliverPorts = getDeliverPorts().values();
        Iterator<Port> deliverPortsIterator = deliverPorts.iterator();


        // Обход портов доставки с учетом весов
        while (portsCollectedValue > 0) {
            if (!deliverPortsIterator.hasNext()) {
                deliverPortsIterator = deliverPorts.iterator(); // Возвращаемся к началу списка
            }

            Port nextPort = deliverPortsIterator.next();
            int portWeight = calculatePortWeight(nextPort);

            if (portsCollectedValue >= portWeight) {
                nextPort.deliver(portWeight);
                portsCollectedValue -= portWeight;
            } else {
                nextPort.deliver(portsCollectedValue);
                portsCollectedValue = 0;
            }
        }
    }
}
