package com.ain.model;

import java.util.Arrays;

public enum OperationType {
    CALC_SEMANAL,
    CLASIFICAR_IMPACTO,
    PROPONER_COMPENSACION;

    public static OperationType fromString(String name) {
        return Arrays.stream(values())
                .filter(op -> op.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
