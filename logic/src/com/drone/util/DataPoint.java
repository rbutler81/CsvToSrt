package com.drone.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataPoint {

    private String value;
    private String unit;

    public DataPoint(String value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public DataPoint(String s) {
        if (s.contains("|")) {
            String[] t = s.split("\\|");
            this.value = t[0];
            this.unit = t[1];
        } else {
            this.value = s;
        }
    }

    public static Map<String, List<DataPoint>> convertListOfStringsToDataPoints(Map<String,List<String>> input) {
        Map<String, List<DataPoint>> r = new HashMap<>();
        for (Map.Entry<String,List<String>> entry : input.entrySet()) {
            List<DataPoint> lodp = new ArrayList<>();
            for (String s : entry.getValue()) {
                lodp.add(new DataPoint(s));
            }
            r.put(entry.getKey(), lodp);
        }
        return r;
    }

    public boolean hasUnits() {
        if (this.unit != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getValue() {
        return value;
    }

    public DataPoint setValue(String value) {
        this.value = value;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public DataPoint setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public String toString(){
        if (hasUnits()) {
            return this.value + this.unit;
        } else {
            return this.value;
        }
    }
}
