package com.example.gostudia.Logic;

import com.example.gostudia.StateField;

import java.util.ArrayList;
import java.util.List;

public class Field {
    private StateField state;
    private List<Field> neighbours;
    private boolean checked;

    public Field() {
        state = StateField.EMPTY;
        neighbours = new ArrayList<>();
    }

    public List<Field> getNeighbours() {
        return neighbours;
    }

    public boolean hasBreaths() {
        if (state != StateField.EMPTY) {
            checked = true;
            boolean own = neighbours.stream().anyMatch(f -> f.getState() == StateField.EMPTY);
            boolean chain = neighbours.stream().anyMatch(f -> f.getState() == state && !f.isChecked() && f.hasBreaths());
            checked = false;
            return own || chain;
        }
        throw new IllegalStateException("Empty field doesn't have breaths");
    }

    public int remove() {
        if (state != StateField.EMPTY) {
            StateField before = state;
            state = StateField.EMPTY;
            if (neighbours.stream().noneMatch(f -> f.getState() == before))
                return 1;

            int sum = 0;
            for (Field neighbour : neighbours) {
                if (neighbour.getState() == before)
                    sum += neighbour.remove();
            }
            return sum + 1;
        }
        throw new IllegalStateException("Empty field cannot be removed");
    }

    public int update(StateField state) {
        if (this.state == state && !hasBreaths())
            return remove();
        return 0;
    }

    public int setState(StateField state) {
        this.state = state;
        if (state != StateField.EMPTY) {
            int sum = 0;
            for (Field neighbour : neighbours) {
                sum += neighbour.update(state.opposite());
            }
            return sum;
        }
        return 0;
    }

    public StateField getState() {
        return state;
    }

    public boolean isChecked() {
        return checked;
    }
}
