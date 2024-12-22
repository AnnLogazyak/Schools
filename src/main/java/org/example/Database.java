package org.example;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static Connection connection;
    private static Statement statement;

    public static void connectDB() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database.db");
        statement = connection.createStatement();
    }

    public static void createTableSchool() throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS Schools (" +
                "district INTEGER," +
                "school TEXT," +
                "country TEXT," +
                "grades TEXT," +
                "students INTEGER," +
                "teachers DOUBLE," +
                "calworks DOUBLE," +
                "lunch DOUBLE," +
                "computer INTEGER," +
                "expenditure DOUBLE," +
                "income DOUBLE," +
                "english DOUBLE," +
                "read DOUBLE," +
                "math DOUBLE)");
    }

    public static void inputDataSchool(ArrayList<? extends School> schools) throws SQLException {
        for(var school : schools)
            statement.execute(String.format("INSERT INTO Schools " +
                    "VALUES ('%d', '%s', '%s', '%s', '%d', '%f', '%f', '%f', '%d', '%f', '%f', '%f', '%f', '%f')",
                    school.district(), school.school(), school.country(), school.grades(), school.students(),
                    school.teachers(), school.calworks(), school.lunch(), school.computer(), school.expenditure(),
                    school.income(), school.english(), school.read(), school.math()));
    }

    public static void deleteData() throws SQLException {
        statement.execute("DROP TABLE IF EXISTS Schools");
    }

    public static ArrayList<School> readData() throws SQLException {
        var resultSet = statement.executeQuery("SELECT * FROM Schools");
        return getDataFromResultSet(resultSet);
    }

    //Количество студентов в стране
    public static int getStudentsCountFilteredByCountry(String country) throws SQLException {
        var resultSet = statement.executeQuery(String.format("SELECT students FROM Schools WHERE country = '%s'", country));
        var sum = 0;
        while(resultSet.next()){
            sum += resultSet.getInt("students");
        }
        return sum;
    }

    //Школы в Fresno, Contra Costa, El Dorado, Glenn, у которых расход больше 10
    public static ArrayList<School> getSchoolsFilteredByCountryAndExpenditure(ArrayList<School> schools) throws SQLException {
        var resultSet = statement.executeQuery("SELECT * FROM Schools " +
                "WHERE country in ('Fresno', 'Contra Costa', 'El Dorado', 'Glenn') " +
                "AND expenditure > 10");
        return getDataFromResultSet(resultSet);
    }

    //Школы с количеством студентов от 5000 до 7500 и от 10000 до 11000 в порядке убывания по math
    public static ArrayList<School> getSchoolsFilteredByStudentsAndMath(ArrayList<School> schools) throws SQLException {
        var resultSet = statement.executeQuery("SELECT * FROM Schools " +
                "WHERE ((5000 <= students AND students <= 7500) OR (10000 <= students AND students <= 11000)) ORDER BY math DESC");
        return getDataFromResultSet(resultSet);
    }

    private static ArrayList<School> getDataFromResultSet(ResultSet resultSet) throws SQLException {
        var schools = new ArrayList<School>();
        while(resultSet.next()){
            var district = resultSet.getInt("district");
            var schoolName = resultSet.getString("school");
            var country = resultSet.getString("country");
            var grades = resultSet.getString("grades");
            var students = resultSet.getInt("students");
            var teachers = resultSet.getDouble("teachers");
            var calworks = resultSet.getDouble("calworks");
            var lunch = resultSet.getDouble("lunch");
            var computer = resultSet.getInt("computer");
            var expenditure = resultSet.getDouble("expenditure");
            var income = resultSet.getDouble("income");
            var english = resultSet.getDouble("english");
            var read = resultSet.getDouble("read");
            var math = resultSet.getDouble("math");

            var school = new School(district, schoolName, country, grades, students,
                    teachers, calworks, lunch, computer, expenditure, income, english,
                    read, math);
            schools.add(school);
        }
        return schools;
    }

    public static void closeDb() throws SQLException {
        statement.close();
        connection.close();
    }
}
