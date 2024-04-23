package ru.ap1kkk.elements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.ap1kkk.ports.Port;

import java.util.HashMap;

@RequiredArgsConstructor
@Getter
public abstract class Element {
    private final Integer id;
    private final HashMap<Integer, Port> receiverPorts;
    private final HashMap<Integer, Port> deliverPorts;

    public abstract void transferData();
    public abstract void earlyUpdate();
    public abstract void update();
    public abstract void process();
}
