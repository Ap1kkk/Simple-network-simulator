package ru.ap1kkk.serialization;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ap1kkk.elements.LoadBalancer;
import ru.ap1kkk.elements.Producer;
import ru.ap1kkk.elements.Receiver;
import ru.ap1kkk.ports.Port;
import ru.ap1kkk.statistic.StatsMode;

import java.util.HashMap;
import java.util.List;

@JsonAutoDetect
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitValues {
    private List<Receiver> receivers;
    private List<Producer> producers;
    private List<LoadBalancer> loadBalancers;
    private Integer iterations;
    private StatsMode statsMode;

    //TODO проверять на соответствие id as key with element id
}
