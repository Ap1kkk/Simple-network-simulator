package ru.ap1kkk.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ap1kkk.elements.ElementFactory;
import ru.ap1kkk.elements.LoadBalancer;
import ru.ap1kkk.ports.Port;
import ru.ap1kkk.ports.PortFactory;
import ru.ap1kkk.ports.PortType;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InitValuesTest {
    private PortFactory portFactory;
    private ElementFactory elementFactory;

    @BeforeEach
    void setUp() {
        elementFactory = new ElementFactory();
        portFactory = new PortFactory();
        elementFactory.clear();
        portFactory.clear();
    }
    @Test
    @SneakyThrows
    public void test() {
        ObjectMapper objectMapper = new ObjectMapper();

        StringWriter writer = new StringWriter();

        Port port = new Port(0, PortType.IN, null);

        HashMap<Integer, Port> ports = new HashMap<>();

        InitValues initValues = new InitValues();

        ports.put(0, port);

        LoadBalancer loadBalancer = new LoadBalancer(0, ports, ports);

        List<LoadBalancer> loadBalancers = new ArrayList<>();
        loadBalancers.add(loadBalancer);
        initValues.setLoadBalancers(loadBalancers);

        objectMapper.writeValue(writer, initValues);
        String convertedString = writer.toString();

        System.out.println(convertedString);

        StringReader reader = new StringReader(convertedString);

        InitValues initValues1 = objectMapper.readValue(reader, InitValues.class);
        initValues1.getLoadBalancers();
    }

    @Test
    @SneakyThrows
    public void testParse() {
        StringBuilder jsonString = new StringBuilder();

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream("init.json")) {

            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader br = new BufferedReader(isr);) {
                String line;
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);
                }
                is.close();
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();

        StringReader reader = new StringReader(jsonString.toString());

        InitValues initValues = objectMapper.readValue(reader, InitValues.class);
        System.out.println(jsonString);
    }

    @Test
    @SneakyThrows
    public void testPortFactory() {
        Port port1 = new Port(0, PortType.IN, null);
        Port port2 = new Port(1, PortType.OUT, 0);

        portFactory.addExistingPort(port1);
        portFactory.addExistingPort(port2);

        portFactory.init();

        HashMap<Integer, Port> receiverPool = PortFactory.getReceiverPool();
        PortFactory.getReceiverPool();
    }

    @Test
    @SneakyThrows
    public void testElementFactory() {
        Port port1 = new Port(0, PortType.IN, null);
        Port port2 = new Port(1, PortType.OUT, 0);

        HashMap<Integer, Port> portMap1 = new HashMap<>();
        portMap1.put(0, port1);

        HashMap<Integer, Port> portMap2 = new HashMap<>();
        portMap2.put(1, port2);

        LoadBalancer loadBalancer1 = new LoadBalancer(0, portMap1, portMap2);
        LoadBalancer loadBalancer2 = new LoadBalancer(1, portMap1, portMap2);

        elementFactory.addLoadBalancer(loadBalancer1);
        elementFactory.addLoadBalancer(loadBalancer2);
    }
}