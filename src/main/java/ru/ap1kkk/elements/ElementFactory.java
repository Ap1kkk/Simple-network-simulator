package ru.ap1kkk.elements;

import lombok.Getter;
import lombok.SneakyThrows;
import ru.ap1kkk.ports.Port;

import java.util.HashMap;

public class ElementFactory {
//    private static ElementFactory instance;
    private static int ID_COUNTER = 0;
    @Getter
    private static final HashMap<Integer, LoadBalancer> loadBalancerPool = new HashMap<Integer, LoadBalancer>();
    @Getter
    private static final HashMap<Integer, Producer> producerPool = new HashMap<Integer, Producer>();
    @Getter
    private static final HashMap<Integer, Receiver> receiverPool = new HashMap<Integer, Receiver>();

//    public ElementFactory() {
//        if(instance == null) {
//            instance = this;
//        }
//    }

    public LoadBalancer buildLoadBalancer(HashMap<Integer, Port> receiverPorts,
                                          HashMap<Integer, Port> deliverPorts) {
        LoadBalancer loadBalancer = new LoadBalancer(ID_COUNTER++, receiverPorts, deliverPorts);
        loadBalancerPool.put(loadBalancer.getId(), loadBalancer);
        return loadBalancer;
    }

    public Producer buildProducer(HashMap<Integer, Port> deliverPorts,
                                  int produceRate) {
        Producer producer = new Producer(
                ID_COUNTER++,
                deliverPorts,
                produceRate);
        producerPool.put(producer.getId(), producer);
        return producer;
    }

    public Receiver buildReceiver(HashMap<Integer, Port> receiverPorts,
                                  int maxLoad,
                                  int processRate) {
        Receiver receiver = new Receiver(
                ID_COUNTER++,
                receiverPorts,
                maxLoad,
                processRate);
        receiverPool.put(receiver.getId(), receiver);
        return receiver;
    }

    @SneakyThrows
    public void addLoadBalancer(LoadBalancer loadBalancer) {
       if(loadBalancerPool.get(loadBalancer.getId()) != null)
           throw new Exception("Load balancer already exists with id: " + loadBalancer.getId());
       loadBalancerPool.put(loadBalancer.getId(), loadBalancer);
    }

    @SneakyThrows
    public void addProducer(Producer producer) {
        if(producerPool.get(producer.getId()) != null)
            throw new Exception("Producer already exists with id: " + producer.getId());
        producerPool.put(producer.getId(), producer);
    }

    @SneakyThrows
    public void addReceiver(Receiver receiver) {
        if(receiverPool.get(receiver.getId()) != null)
            throw new Exception("Receiver already exists with id: " + receiver.getId());
        receiverPool.put(receiver.getId(), receiver);
    }
}
