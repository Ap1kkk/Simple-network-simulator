package ru.ap1kkk.elements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.RequiredArgsConstructor;
import ru.ap1kkk.ports.Port;

import java.util.HashMap;

@JsonAutoDetect
public class LoadBalancer extends Element {
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

    }

    @Override
    public void update() {
    }

    @Override
    public void process() {
    }
}
