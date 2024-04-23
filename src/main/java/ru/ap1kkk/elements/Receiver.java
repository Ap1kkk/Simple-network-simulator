package ru.ap1kkk.elements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.ap1kkk.ports.Port;

import java.util.HashMap;

@JsonAutoDetect
public class Receiver extends Element {
    @JsonProperty("maxLoad")
    private final Integer maxLoad;
    @JsonProperty("processRate")
    private final Integer processRate;

    private int currentValue = 0;

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
            currentValue += port.getReceivedValue();
        }
    }

    @Override
    public void process() {
        currentValue -= processRate;
        if (currentValue < 0)
            currentValue = 0;
    }
}
