package edu.eci.tdse.parcial;

import org.springframework.stereotype.Service;

import java.util.StringJoiner;

@Service
public class MathService {
    public MathResponse calculate(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("El valor debe ser un entero no negativo");
        }

        if (value == 0) {
            return new MathResponse("Secuencia de Lucas", 0, "2");
        }

        StringJoiner joiner = new StringJoiner(", ");
        long previous = 2;
        long current = 1;

        joiner.add(String.valueOf(previous));
        joiner.add(String.valueOf(current));

        for (long i = 2; i <= value; i++) {
            long next = previous + current;
            joiner.add(String.valueOf(next));
            previous = current;
            current = next;
        }

        return new MathResponse("Secuencia de Lucas", value, joiner.toString());
    }
}
