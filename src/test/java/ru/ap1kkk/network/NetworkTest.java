package ru.ap1kkk.network;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ap1kkk.elements.ElementFactory;
import ru.ap1kkk.ports.PortFactory;

@ExtendWith(MockitoExtension.class)
class NetworkTest {
    private PortFactory portFactory;
    private ElementFactory elementFactory;
    private Network network;

    @BeforeEach
    void setUp() {
        elementFactory = new ElementFactory();
        portFactory = new PortFactory();
        elementFactory.clear();
        portFactory.clear();
    }

    @Test
    void initialize_success() {
        network = buildNetwork("init_success.json");
        Assertions.assertNotNull(network);

        Assertions.assertDoesNotThrow(() -> {
            network.initialize();
        });

        Assertions.assertNotNull(network.getStatsMode());
        Assertions.assertNotEquals(0, ElementFactory.getProducerPool().size());
        Assertions.assertNotEquals(0, ElementFactory.getLoadBalancerPool().size());
        Assertions.assertNotEquals(0, ElementFactory.getReceiverPool().size());
        Assertions.assertNotEquals(0, PortFactory.getReceiverPool().size());
        Assertions.assertNotEquals(0, PortFactory.getDeliverPool().size());
    }

    @Test
    void initialize_fail_port_out() {
        network = buildNetwork("init_failed_port_out.json");
        Assertions.assertNotNull(network);

        Assertions.assertThrows(Exception.class, () -> {
            network.initialize();
        });
    }

    @Test
    void initialize_fail_receiver_no_receiver_ports() {
        network = buildNetwork("init_failed_receiver_no_receiver_ports.json");
        Assertions.assertNotNull(network);

        Assertions.assertThrows(Exception.class, () -> {
            network.initialize();
        });
    }

    @Test
    void initialize_fail_producer_no_deliver_ports() {
        network = buildNetwork("init_failed_producer_no_deliver_ports.json");
        Assertions.assertNotNull(network);

        Assertions.assertThrows(Exception.class, () -> {
            network.initialize();
        });
    }

    @Test
    void initialize_fail_producer_produce_rate_less_than_zero() {
        network = buildNetwork("init_failed_producer_produce_rate_less_than_zero.json");
        Assertions.assertNotNull(network);

        Assertions.assertThrows(Exception.class, () -> {
            network.initialize();
        });
    }

    @Test
    void initialize_fail_load_balancer_no_deliver_ports() {
        network = buildNetwork("init_failed_load_balancer_no_deliver_ports.json");
        Assertions.assertNotNull(network);

        Assertions.assertThrows(Exception.class, () -> {
            network.initialize();
        });
    }

    @Test
    void initialize_fail_load_balancer_no_receiver_ports() {
        network = buildNetwork("init_failed_load_balancer_no_receiver_ports.json");
        Assertions.assertNotNull(network);

        Assertions.assertThrows(Exception.class, () -> {
            network.initialize();
        });
    }

    private Network buildNetwork(String initFile) {
        return new Network(initFile, portFactory, elementFactory);
    }
}