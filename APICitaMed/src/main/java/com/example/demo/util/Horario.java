package com.example.demo.util;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class Horario {
    public static class Horas {
        private final LocalTime inicio;
        private final LocalTime fin;
        public Horas(LocalTime inicio, LocalTime fin) {
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

    public static final List<Horas> HORAS = Arrays.asList(
        new Horas(LocalTime.of(8, 0), LocalTime.of(9, 0)),
        new Horas(LocalTime.of(9, 0), LocalTime.of(10, 0)),
        new Horas(LocalTime.of(10, 0), LocalTime.of(11, 0)),
        new Horas(LocalTime.of(11, 0), LocalTime.of(12, 0)),
        new Horas(LocalTime.of(12, 0), LocalTime.of(13, 0)),
        new Horas(LocalTime.of(13, 0), LocalTime.of(14, 0)),
        new Horas(LocalTime.of(14, 0), LocalTime.of(15, 0))
    );

    private Horario() {
    }
}
