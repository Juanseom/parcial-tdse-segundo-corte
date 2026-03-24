package edu.eci.tdse.parcial;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MathService {
    public MathResponse calculate(long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("El valor debe ser un entero positivo");
        }

        List<Long> sequence = new ArrayList<>();
        long current = value;
        sequence.add(current);

        while (current != 1) {
            current = (current % 2 == 0) ? current / 2 : (current * 3) + 1;
            sequence.add(current);
        }
        return new MathResponse(value, sequence.size() - 1, sequence);
    }
}
