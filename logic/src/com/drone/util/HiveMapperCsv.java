package com.drone.util;

import csvUtils.CSVWriter;

import java.util.ArrayList;
import java.util.List;

public class HiveMapperCsv extends SparkSrtFrame implements CSVWriter {

    private long timeStampCsv;

    public HiveMapperCsv(){};

    public HiveMapperCsv(SparkSrtFrame ssf) {
        super(ssf);
    }

    public static HiveMapperCsv createNewHiveMapperCsv(SparkSrtFrame ssf, long startTime, long endTime, long timeStamp) {
        if ((ssf.getStartTime() >= startTime) && (ssf.getStartTime() <= endTime)) {
            HiveMapperCsv h =  new HiveMapperCsv(ssf);
            return h.setTimeStampCsv(timeStamp);
        } else {
            return null;
        }
    }

    public long getTimeStampCsv() {
        return timeStampCsv;
    }

    public HiveMapperCsv setTimeStampCsv(long timeStampCsv) {
        this.timeStampCsv = timeStampCsv + this.getStartTime();
        return this;
    }

    @Override
    public List<String[]> toCSV(List<?> l) {
        List<String[]> r = new ArrayList<>();
        String[] headers = {"sensorLatitude", "sensorLongitude", "sensorTrueAltitude", "timeframeBegin", "timeframeEnd", "timestamp"};
        r.add(headers);

        List<HiveMapperCsv> hmc = (List<HiveMapperCsv>) l;

        for (HiveMapperCsv h : hmc) {

            double bD = (double) h.getStartTime();
            double eD = (double) h.getEndTime();
            bD = bD / 1000;
            eD = eD / 1000;
            String bS = Double.toString(bD);
            String t[] = bS.split("\\.");
            bS = t[0] + "." + padZerosToEnd(3, t[1]);
            String eS = Double.toString(eD);
            String u[] = eS.split("\\.");
            eS = u[0] + "." + padZerosToEnd(3, u[1]);

            r.add(new String[]{
                    h.getData().get("GPS").get(0).getValue(),
                    h.getData().get("GPS").get(1).getValue(),
                    h.getData().get("A").get(0).getValue(),
                    bS,
                    eS,
                    Long.toString(h.getTimeStampCsv())
            });
        }

        return r;
    }

    private static String padZerosToEnd(int totalNumber, String s) {
        String r = s;
        if (r.length() < totalNumber) {
            int loops = totalNumber - r.length();
            for (int i = 0; i < loops; i++) {
                r = r + "0";
            }
        }
        return r;
    }
}
