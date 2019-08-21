package com.company;

import java.util.HashSet;
import java.util.Set;

public class StreetData {
    private String name;
    private int count;
    private Set<String> roadTypesSet;

    public StreetData(String name, String roadType) {
        this.name = name;
        count = 1;
        roadTypesSet = new HashSet<>();
        roadTypesSet.add(roadType);
    }

    public void countIncrement() {
        count += 1;
    }

    public void addRoadType(String roadType) {
        roadTypesSet.add(roadType);
    }


    @Override
    public String toString() {
        return String.format("%-30s %2d %s", name, count, roadTypesSet);
    }
}
