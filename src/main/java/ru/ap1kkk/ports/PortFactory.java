package ru.ap1kkk.ports;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.HashMap;

@Getter
public class PortFactory {
//    private static PortFactory instance;
    //TODO пофиксить создание через фабрику
    private static int ID_COUNTER = 0;
    @Getter
    private static final HashMap<Integer, Port> receiverPool = new HashMap<Integer, Port>();
    @Getter
    private static final HashMap<Integer, Port> deliverPool = new HashMap<Integer, Port>();

//    public PortFactory() {
//        if(instance == null) {
//            instance = this;
//        }
//    }

    public Port buildReceiver() {
        Port port = new Port(ID_COUNTER++, PortType.IN , null);
        receiverPool.put(port.getId(), port);
        return port;
    }

    public Port buildDeliver(Integer connectedPortId) {
        Port port = new Port(ID_COUNTER++, PortType.OUT , connectedPortId);
        deliverPool.put(port.getId(), port);
        return port;
    }

    @SneakyThrows
    public void addExistingPort(Port port) {
        if(safeFindReceiver(port.getId()) != null)
            throw new Exception("already exists port with id: " + port.getId());

        if(port.getType().equals(PortType.IN))
            receiverPool.put(port.getId(), port);
        else
            deliverPool.put(port.getId(), port);
    }

    public void validatePorts() {
        for(Port port: deliverPool.values()) {
            port.validate();
        }
        for(Port port: receiverPool.values()) {
            port.validate();
        }
    }

    public void fillConnectedPorts() {
        for(Port port: deliverPool.values()) {
            Port receiver = findReceiver(port.getConnectedPortId());
            port.fillConnectedPort(receiver);
        }
    }

    @SneakyThrows
    public Port findReceiver(int id) {
        Port port = receiverPool.get(id);
        if(port == null)
            throw new Exception("unable to find receiver port by id: " + id);
        return port;
    }

    private Port safeFindReceiver(int id) {
        Port port = receiverPool.get(id);
        if(port != null)
            return port;

        return deliverPool.get(id);
    }
}
