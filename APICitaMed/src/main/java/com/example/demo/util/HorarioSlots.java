package com.example.demo.util;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class HorarioSlots {
    public static class Slot {
        private final LocalTime inicio;
        private final LocalTime fin;
        public Slot(LocalTime inicio, LocalTime fin) {
            this.inicio = inicio;
            this.fin = fin;
        }
        public LocalTime getInicio() {
            return inicio;
        }
        public LocalTime getFin() {
            return fin;
        }
        @Override
        public String toString() {
            return inicio + " - " + fin;
        }
    }

    public static final List<Slot> SLOTS_FRIOS = Arrays.asList(
        new Slot(LocalTime.of(8, 0), LocalTime.of(9, 0)),
        new Slot(LocalTime.of(9, 0), LocalTime.of(10, 0)),
        new Slot(LocalTime.of(10, 0), LocalTime.of(11, 0)),
        new Slot(LocalTime.of(11, 0), LocalTime.of(12, 0)),
        new Slot(LocalTime.of(12, 0), LocalTime.of(13, 0)),
        new Slot(LocalTime.of(13, 0), LocalTime.of(14, 0)),
        new Slot(LocalTime.of(14, 0), LocalTime.of(15, 0))
    );

    private HorarioSlots() {
    }
}
