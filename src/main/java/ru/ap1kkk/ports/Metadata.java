package ru.ap1kkk.ports;

public record Metadata (
        int elementMaxLoad,
        int elementCurrentLoad,
        int elementProcessRate
){
}
