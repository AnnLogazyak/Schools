package org.example;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        //Считывание данных из файла
        var path = "C:\\temp\\Школы.csv";
        List<String[]> data = SchoolHandler.readAllLines(path);

        //Создание всех школ
        var schools = SchoolHandler.getSchools(data);

        String[] countries = {"Alameda", "Butte", "Tulare", "Ventura", "Madera", "El Dorado", "Lake", "Sonoma", "Nevada", "Kern"};

        //Работа с базой данных
        ArrayList<Integer> studentsCount = new ArrayList<>();
        ArrayList<School> schoolsFilteredByExpenditure;
        ArrayList<School> schoolsFilteredByStudentsAndMath;
        try {
            Database.connectDB();
            Database.deleteData();
            Database.createTableSchool();
            Database.inputDataSchool(schools);

            for (var country : countries) {
                studentsCount.add(Database.getStudentsCountFilteredByCountry(country));
            }
            schoolsFilteredByExpenditure = Database.getSchoolsFilteredByCountryAndExpenditure(schools);
            schoolsFilteredByStudentsAndMath = Database.getSchoolsFilteredByStudentsAndMath(schools);

            Database.closeDb();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Количество студентов в 10 разных странах и график (Задача 1)
        for (var i = 0; i < countries.length; i++){
            System.out.println("Страна: " + countries[i] + ", Количество студентов: " + studentsCount.get(i));
        }
        Map<String, Integer> map = SchoolHandler.getStudentsCount(studentsCount, countries);
        Graph graph = new Graph(map);
        graph.setVisible(true);

        //Среднее количество расходов в Fresno, Contra Costa, El Dorado, Glenn, у которых расход больше 10 (Задача 2)
        var averageExpenditure = 0.0;
        for (var school : schoolsFilteredByExpenditure)
            averageExpenditure += school.expenditure();
        averageExpenditure /= schoolsFilteredByExpenditure.size();
        System.out.println("Среднее количество расходов: " + averageExpenditure);

        //Школа с нужным числом студентов и максимальным баллом по математике (Задача 3)
        System.out.println("Учебное заведение: " + schoolsFilteredByStudentsAndMath.get(0).school());
    }
}