package ru.whitebeef.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Group<V> {

    private Group<V> parent = null;
    private Set<Pair<V, Integer>> values = new HashSet<>();
    private Set<Integer> lineIndexes = null;

    public Group(Pair<V, Integer> value) {
        values.add(value);
    }

    public void addValue(Pair<V, Integer> value) {
        values.add(value);
    }

    public boolean isInGroup(Pair<V, Integer> value) {
        return values.contains(value);
    }

    public Set<Pair<V, Integer>> getValues() {
        return values;
    }

    public int getSize() {
        return values.size();
    }

    public Group<V> getParent() {
        return parent;
    }

    public Group<V> unionGroup(Group<V> group) {
        if (group == this) {
            return this;
        }
        while (group.getParent() != null) {
            group = group.getParent();
        }
        if (getSize() > group.getSize()) {
            values.addAll(group.values);
            lineIndexes.addAll(group.lineIndexes);
            group.values = null;
            group.lineIndexes = null;
            group.parent = this;
            return this;
        } else {
            group.values.addAll(values);
            group.lineIndexes.addAll(lineIndexes);
            values = null;
            lineIndexes = null;
            parent = group;
            return group;
        }
    }

    public void addLineIndex(int index) {
        if (lineIndexes == null) {
            lineIndexes = new TreeSet<>();
        }
        lineIndexes.add(index);
    }

    public int getLinesSize() {
        if (lineIndexes == null) {
            return 0;
        }
        return lineIndexes.size();
    }

    public Set<Integer> getLineIndexes() {
        return lineIndexes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group<?> group = (Group<?>) o;
        return Objects.equals(parent, group.parent) && Objects.equals(values, group.values) && Objects.equals(lineIndexes, group.lineIndexes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, values, lineIndexes);
    }

    @Override
    public String toString() {
        return "Group{" +
                ", numbers=" + values +
                ", lineIndexes=" + lineIndexes +
                '}';
    }
}
