package com.example.gostudia.Logic;

import com.example.gostudia.StateField;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GoField {
    private StateField state;
    private final List<GoField> neighbours;
    private boolean checked;
    private boolean alive;
    private StateField territory = null;

    public GoField() {
        state = StateField.EMPTY;
        neighbours = new ArrayList<>();
    }

    public GoField(StateField color) {
        state = color;
        neighbours = new ArrayList<>();
    }

    public List<GoField> getNeighbours() {
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
            for (GoField neighbour : neighbours) {
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
            for (GoField neighbour : neighbours) {
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

    public boolean isAlive() {
        if (state == StateField.EMPTY)
            return false;
        return alive;
    }

    public void setAlive(boolean alive) {
        if (state != StateField.EMPTY)
            this.alive = alive;
    }

    public StateField getTerritory() {
        if (state != StateField.EMPTY)
            return null;
        return territory;
    }

    public void setTerritory(StateField territory) {
        if (state == StateField.EMPTY)
            this.territory = territory;
    }

    public Set<GoField> checkTerritory() {
        Set<GoField> set = new HashSet<>();
        set.add(this);
        if (state == StateField.EMPTY) {
            checked = true;
            for (GoField neighbour : neighbours) {
                if (!neighbour.isChecked())
                    set.addAll(neighbour.checkTerritory());
            }
        }
        return set;
    }
}
