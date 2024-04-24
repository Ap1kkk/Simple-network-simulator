package ru.ap1kkk.elements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.log4j.Logger;
import ru.ap1kkk.ports.Port;

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
        currentLoad -= processRate;
        if (currentLoad < 0)
            currentLoad = 0;

        System.out.printf("Receiver with id: %s -- processed load: %s%n", getId(), currentLoad);
//        logger.debug(String.format("Receiver with id: %s -- processed load: %s%n", getId(), currentLoad));
    }
}
