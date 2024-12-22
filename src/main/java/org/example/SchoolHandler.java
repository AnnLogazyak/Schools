package org.example;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchoolHandler {
    public static ArrayList<School> getSchools(List<String[]> data){
        var schools = new ArrayList<School>();
        for (var arr : data){
            var st = arr[0].replaceAll("\"", "").split(",");
            schools.add(new School(Integer.parseInt(st[1]), st[2], st[3], st[4],
                    Integer.parseInt(st[5]), Double.parseDouble(st[6]), Double.parseDouble(st[7]), Double.parseDouble(st[8]),
                    Integer.parseInt(st[9]), Double.parseDouble(st[10]), Double.parseDouble(st[11]), Double.parseDouble(st[12]),
                    Double.parseDouble(st[13]), Double.parseDouble(st[14])));
        }
        return  schools;
    }

    public static List<String[]> readAllLines(String filePath) throws Exception {
        try (var reader = new BufferedReader(new FileReader(filePath))) {
            try (var csvReader = new CSVReader(reader)) {
                csvReader.readNext();
                return csvReader.readAll();
            }
        }
    }

    public static Map<String, Integer> getStudentsCount(ArrayList<Integer> students, String[] countries){
        Map<String, Integer> map = new HashMap<>();
        for (var i = 0; i < countries.length; i++){
            map.put(countries[i], students.get(i));
        }
        return map;
    }
}
