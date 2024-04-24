package ru.ap1kkk.ports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.naming.OperationNotSupportedException;

@RequiredArgsConstructor
@Getter
@JsonAutoDetect
public class Port {
    private final Integer id;
    private final PortType type;
    private final Integer connectedPortId;
    private Port connectedPort;
    private int receivedValue = 0;

    private Port() {
        id = null;
        type = null;
        connectedPortId = null;
    }

    @SneakyThrows
    public void fillConnectedPort(Port port) {
        if(!connectedPortId.equals(port.id))
            throw new OperationNotSupportedException("connectedPortId does not match given port id");
        if(!port.getType().equals(PortType.IN))
            throw new OperationNotSupportedException("connected port must be receiver");
        connectedPort = port;
    }

    @SneakyThrows
    public void deliver(int load) {
        System.out.printf("Port with id: %s -- deliver load: %s -- to: %s%n", getId(), load, connectedPortId);
        if(type.equals(PortType.IN))
            throw new OperationNotSupportedException("IN port cannot deliver");
        connectedPort.receive(load);
    }

    @SneakyThrows
    public void receive(int load) {
        if(type.equals(PortType.OUT))
            throw new OperationNotSupportedException("OUT port cannot receive");
        receivedValue += load;
        System.out.printf("Port with id: %s -- receive load: %s%n", getId(), load);
    }

    public void reset() {
        receivedValue = 0;
    }

    @SneakyThrows
    public void validate() {
        if(id == null)
            throw new Exception("Port id is null");
        if(type == null)
            throw new Exception("Port type is null");

        if(type.equals(PortType.OUT)) {
            if(connectedPortId == null)
                throw new Exception("Port OUT connectedPortId is null");
            if(connectedPort == null)
                throw new Exception("Port OUT connectedPort is null");
        }
    }
}
