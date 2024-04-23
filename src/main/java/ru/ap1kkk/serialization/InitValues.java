package ru.ap1kkk.serialization;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ap1kkk.elements.LoadBalancer;
import ru.ap1kkk.elements.Producer;
import ru.ap1kkk.elements.Receiver;
import ru.ap1kkk.ports.Port;

import java.util.HashMap;

@JsonAutoDetect
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitValues {
    private HashMap<Integer, Receiver> receivers;
    private HashMap<Integer, Producer> producers;
    private HashMap<Integer, LoadBalancer> loadBalancers;

    //TODO проверять на соответствие id as key with element id
}
