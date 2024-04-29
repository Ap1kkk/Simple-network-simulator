package ru.ap1kkk.elements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import ru.ap1kkk.ports.Port;

import java.util.HashMap;

@Getter
public abstract class Element {
//    public static final Logger logger = Logger.getLogger(Element.class.getName());

    private final Integer id;
    private final HashMap<Integer, Port> receiverPorts;
    private final HashMap<Integer, Port> deliverPorts;

    public Element(Integer id,
    HashMap<Integer, Port> receiverPorts,
    HashMap<Integer, Port> deliverPorts) {
        this.id = id;
        this.receiverPorts = receiverPorts;
        this.deliverPorts = deliverPorts;

//        BasicConfigurator.configure();
    }

    public abstract void transferData();
    public abstract void earlyUpdate();
    public abstract void update();
    public abstract void process();
    public abstract void validate();

    public void resetPortValues() {
        resetPortValues(receiverPorts);
        resetPortValues(deliverPorts);
    }
    private void resetPortValues(HashMap<Integer, Port> ports) {
        if(ports != null) {
            for (Port port: ports.values()) {
                port.reset();
            }
        }
    }
}
