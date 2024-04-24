package ru.ap1kkk.elements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.ap1kkk.ports.Port;

import java.util.HashMap;

@JsonAutoDetect
public class Producer extends Element {
    @JsonProperty("produceRate")
    private final Integer produceRate;

    private Producer() {
        super(null, null, null);
        produceRate = null;
    }
    public Producer(
            int id,
            HashMap<Integer, Port> deliverPorts,
            int produceRate
    ) {
        super(id, null, deliverPorts);
        this.produceRate = produceRate;
    }

    @Override
    public void transferData() {
        for(Port port: getDeliverPorts().values()) {
            port.deliver(produceRate);
        }
    }

    @Override
    public void earlyUpdate() {
    }

    @Override
    public void update() {
    }

    @Override
    public void process() {
    }
}
