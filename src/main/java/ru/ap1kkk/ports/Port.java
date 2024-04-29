package ru.ap1kkk.ports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import javax.naming.OperationNotSupportedException;

@RequiredArgsConstructor
@Getter
@JsonAutoDetect
public class Port {
    private final Integer id;
    private final PortType type;
    private final Integer connectedPortId;
    @Setter
    private Metadata metadata;
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
            throw new OperationNotSupportedException(String.format("Port %s: connectedPortId does not match given port id", getId()));
        if(!port.getType().equals(PortType.IN))
            throw new OperationNotSupportedException(String.format("Port %s: connected port must be receiver", getId()));
        connectedPort = port;
    }

    @SneakyThrows
    public void deliver(int load) {
//        System.out.printf("Port with id: %s -- deliver load: %s -- to: %s%n", getId(), load, connectedPortId);
        if(type.equals(PortType.IN))
            throw new OperationNotSupportedException(String.format("Port %s: IN port cannot deliver", getId()));
        connectedPort.receive(load);
    }

    @SneakyThrows
    public void receive(int load) {
        if(type.equals(PortType.OUT))
            throw new OperationNotSupportedException(String.format("Port %s: OUT port cannot receive", getId()));
        receivedValue += load;
//        System.out.printf("Port with id: %s -- receive load: %s%n", getId(), load);
    }

    public void reset() {
        receivedValue = 0;
    }

    @SneakyThrows
    public void validate() {
        if(id == null)
            throw new Exception(String.format("Port %s: Port id is null", getId()));
        if(type == null)
            throw new Exception(String.format("Port %s: Port type is null", getId()));

        if(type.equals(PortType.OUT)) {
            if(connectedPortId == null)
                throw new Exception(String.format("Port %s: Port OUT connectedPortId is null", getId()));
            if(connectedPort == null)
                throw new Exception(String.format("Port %s: Port OUT connectedPort is null", getId()));
        }
    }
}
