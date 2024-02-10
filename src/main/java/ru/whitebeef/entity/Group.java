package ru.whitebeef.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Group {

    private Group parent = null;
    private Set<Long> numbers = new HashSet<>();
    private Set<Integer> lineIndexes = null;

    public Group(Long value) {
        numbers.add(value);
    }

    public void addNumber(Long number) {
        numbers.add(number);
    }

    public boolean isInGroup(Long value) {
        return numbers.contains(value);
    }

    public int getSize() {
        return numbers.size();
    }

    public Group getParent() {
        return parent;
    }

    public Group unionGroup(Group group) {
        if (group == this) {
            return this;
        }
        if (group.getParent() != null) {
            return unionGroup(group.getParent());
        }
        if (getSize() > group.getSize()) {
            numbers.addAll(group.numbers);
            lineIndexes.addAll(group.lineIndexes);
            group.numbers = null;
            group.lineIndexes = null;
            group.parent = this;
            return this;
        } else {
            group.numbers.addAll(numbers);
            group.lineIndexes.addAll(lineIndexes);
            numbers = null;
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
        Group group = (Group) o;
        return Objects.equals(parent, group.parent) && Objects.equals(numbers, group.numbers) && Objects.equals(lineIndexes, group.lineIndexes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, numbers, lineIndexes);
    }

    @Override
    public String toString() {
        return "Group{" +
                ", numbers=" + numbers +
                ", lineIndexes=" + lineIndexes +
                '}';
    }
}
