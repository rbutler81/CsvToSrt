package com.drone.util;

import csvUtils.CSVWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SparkSrtFrame extends SrtFrame {

    private Map<String,List<DataPoint>> data;

    public SparkSrtFrame() {}

    public SparkSrtFrame(SparkSrtFrame ssf) {
        this.data = ssf.getData();
        super.setStartTime(ssf.getStartTime());
        super.setEndTime(ssf.getEndTime());
        super.setText(ssf.getText());
        super.setFrame(ssf.getFrame());
    }

    public SparkSrtFrame(Map<String,String> data, Map<String,List<DataPoint>> headersToKeep) {

        this.data = new HashMap<>();
        // loop through the headers to search for
        for (Map.Entry<String, List<DataPoint>> entry : headersToKeep.entrySet()) {
            // loop through the template data points to search for within one entry
            List<DataPoint> lodp = new ArrayList<>();
            for (DataPoint htk : entry.getValue()) {
                // loop through single line of csv (all data)
                for (Map.Entry<String, String> rd : data.entrySet()) {
                    if (rd.getKey().equals(htk.getValue())) {
                        lodp.add(createDataPointFromTemplate(htk, rd.getValue()));
                    }
                }
            }
            if (lodp.size() > 0) {
                this.data.put(entry.getKey(), lodp);
            }
        }
        if (this.data.containsKey("T")) {
            super.setStartTime(Long.parseLong(this.data.get("T").get(0).getValue()));
        }

    }

    public Map<String, List<DataPoint>> getData() {
        return data;
    }

    public SparkSrtFrame setData(Map<String, List<DataPoint>> data) {
        this.data = data;
        return this;
    }

    private DataPoint createDataPointFromTemplate(DataPoint template, String value) {
        return new DataPoint(value, template.getUnit());
    }

    private String createParamSetString(String label, List<DataPoint> data) {
        String dataGroup = "(";

        for (int i = 1; i <= data.size(); i++) {
            if (i == 1 && (data.size() == 1)) {
                dataGroup = dataGroup + data.get(i-1).toString() + ") ";
            } else if (i < data.size()) {
                dataGroup = dataGroup + data.get(i-1).toString() + ",";
            } else {
                dataGroup = dataGroup + data.get(i-1).toString() + ") ";
            }
        }

        return label + dataGroup;
    }

    public String toString() {
        String text = "";
        for (Map.Entry<String,List<DataPoint>> d : this.data.entrySet()) {
            text = text + createParamSetString(d.getKey(),d.getValue());
        }
        super.setText(text);
        return super.toString();
     }




}
