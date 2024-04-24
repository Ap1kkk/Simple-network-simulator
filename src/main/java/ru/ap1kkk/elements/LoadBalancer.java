package ru.ap1kkk.elements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.RequiredArgsConstructor;
import ru.ap1kkk.ports.Port;

import java.util.Collection;
import java.util.HashMap;

@JsonAutoDetect
public class LoadBalancer extends Element {
    private int lastDeliveredPortIndex = -1;
    private int portsCollectedValue = 0;

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
        collectPortsValue();

        Collection<Port> deliverPorts = getDeliverPorts().values();

        // Если нет портов доставки, выходим из метода
        if (deliverPorts.isEmpty()) {
            return;
        }

        // Определяем индекс следующего порта
        lastDeliveredPortIndex++;
        if (lastDeliveredPortIndex >= deliverPorts.size()) {
            lastDeliveredPortIndex = 0; // Возвращаемся к началу списка
        }

        // Получаем порт, на который будем направлять данные
        deliverPorts.stream()
                .skip(lastDeliveredPortIndex)
                .findFirst()
                .ifPresent(
                        nextPort -> nextPort.deliver(portsCollectedValue)
                );
    }

    private void collectPortsValue() {
        portsCollectedValue = 0;
        for (Port port: getReceiverPorts().values()) {
            portsCollectedValue += port.getReceivedValue();
        }
    }

    @Override
    public void update() {
    }

    @Override
    public void process() {
    }
}
