package com.drone.util;

import configFileUtil.Config;
import csvUtils.CSVUtil;

import java.io.*;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Main {

    // setup static variables ///////////////////////////////////////////////////////////////////
    static final String PATH = Paths.get(".").toAbsolutePath().normalize().toString() + "\\";

    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        final long TIMESTAMP = System.currentTimeMillis();
        final long DATA_STARTS = Long.parseLong(args[1]) * 1000;
        final long DATA_ENDS = Long.parseLong(args[2]) * 1000;
        final String OUTPUT_CSV = PATH + "output.csv";

        // check for csv file
        final String CSV_FILE = PATH + args[0];
        File csvFile = new File(CSV_FILE);
        if (!csvFile.exists()) {
            throw new FileNotFoundException("File not found in current folder: " + args[0]);
        }

        // read csv file into a list of maps
        List<String[]> csvAsStringArray = CSVUtil.read(CSV_FILE,",");
        List<Map<String,String>> data = CSVUtil.collectDataByHeaderRow(csvAsStringArray);
        csvAsStringArray = null;

        // build parameter dictionary
        final String CSV_PARAM_FILE = PATH + "csv_params.ini";
        File paramFile = new File(CSV_PARAM_FILE);
        if (!paramFile.exists()) {
            throw new FileNotFoundException("File not found: " + paramFile);
        }
        final Config CONFIG_PARAMS = Config.readIniFile(CSV_PARAM_FILE);
        Map<String, List<DataPoint>> headersToFilter =  DataPoint.convertListOfStringsToDataPoints(CONFIG_PARAMS.getParams());

        // create list of srt frames
        List<SparkSrtFrame> lossrt = new ArrayList<>();
        for (Map<String,String> d : data) {
            lossrt.add(new SparkSrtFrame(d, headersToFilter));
        }

        for (int i = 0; i < lossrt.size(); i++) {
            lossrt.get(i).setFrame(i+1);
            if (i < (lossrt.size() - 1)) {
                lossrt.get(i).setEndTime(lossrt.get(i+1).getStartTime() - 1);
            } else {
                lossrt.get(i).setEndTime(lossrt.get(i).getStartTime() + 1000);
            }

        }

        // SRT write to output file
        for (SparkSrtFrame ssf : lossrt) {
            writeToFile(ssf.toString());
        }

        // collect all SRT frames for the data window asked for
        List<HiveMapperCsv> hmc = new ArrayList<>();
        for (SparkSrtFrame s : lossrt) {
            HiveMapperCsv h = HiveMapperCsv.createNewHiveMapperCsv(s, DATA_STARTS, DATA_ENDS, TIMESTAMP);
            if (h != null) {
                hmc.add(h);
            }
        }

        // write csv file
        CSVUtil.writeObject(hmc, OUTPUT_CSV, ",");

        System.out.println();
    }

    public static void writeToFile(String s) throws IOException {

        BufferedWriter bw = null;

        try {
            FileWriter fstream = new FileWriter("output.srt", true);
            bw = new BufferedWriter(fstream);
            bw.write(s);
        }

        catch (IOException e) {
            throw e;
        }

        finally {
            if(bw != null) {
                bw.close();
            }
        }
    }

}
