package ru.ap1kkk.elements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import ru.ap1kkk.ports.Metadata;
import ru.ap1kkk.ports.Port;
import ru.ap1kkk.statistic.StatisticCollector;

import java.net.PortUnreachableException;
import java.util.HashMap;

@JsonAutoDetect
public class Receiver extends Element {
    @JsonProperty("maxLoad")
    private final Integer maxLoad;
    @JsonProperty("processRate")
    private final Integer processRate;

    private int currentLoad = 0;

    private Receiver() {
        super(null, null, null);
        maxLoad = null;
        processRate = null;
    }
    public Receiver(
            int id,
            HashMap<Integer, Port> receiverPorts,
            int maxLoad,
            int processRate
    ) {
        super(id, receiverPorts, null);
        this.maxLoad = maxLoad;
        this.processRate = processRate;
    }

    public void init() {
        StatisticCollector.setMaxLoadRate(getId(), maxLoad);
        updatePortsMetadata();
    }

    private void updatePortsMetadata() {
        for (Port port: getReceiverPorts().values()) {
            port.setMetadata(new Metadata(maxLoad, currentLoad, processRate));
        }
    }

    @Override
    public void transferData() {
    }

    @Override
    public void earlyUpdate() {
    }

    @Override
    public void update() {
        for(Port port: getReceiverPorts().values()) {
            currentLoad += port.getReceivedValue();
        }
//        System.out.printf("Receiver with id: %s -- current update load: %s%n", getId(), currentLoad);
    }

    @Override
    public void process() {
        System.out.printf("Receiver with id: %s -- current load: %s%n", getId(), currentLoad);
        StatisticCollector.addLoadStats(getId(), currentLoad);
        currentLoad -= processRate;
        if (currentLoad < 0)
            currentLoad = 0;

        System.out.printf("Receiver with id: %s -- unprocessed load: %s%n", getId(), currentLoad);
        updatePortsMetadata();
        StatisticCollector.addProcessedStats(getId(), currentLoad);
    }

    @Override
    @SneakyThrows
    public void validate() {
        if(getReceiverPorts() == null || getReceiverPorts().isEmpty())
            throw new Exception(String.format("Receiver %s: receiver ports must not be null", getId()));
        if(maxLoad <= 0)
            throw new Exception(String.format("Receiver %s: max load must be greater than zero", getId()));
    }
}
