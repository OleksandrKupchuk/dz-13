package dbqueries;

import person.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueriesPostgres {
    private final static String URL = "jdbc:postgresql://localhost:5432/postgres";
    private final static String USER_NAME = "postgres";
    private final static String USER_PASSWORD = "postgres";

    public static void main(String[] args) {
        clearPersons();
        List<Person> persons = new ArrayList() {{
            add(new Person(1, "Oleg", "Opefan", 18));
            add(new Person(2, "Sam", "Diver", 21));
            add(new Person(3, "Clark", "Rodger", 55));
        }};

        insertPersons(persons);
        updatePersonById(2, new Person("Oleg", "Opefan", 19));
        deletePersonById(1);
        List<Person> updatePersons = getAllPersons();

        for (Person person : updatePersons) {
            System.out.println(String.format("id = %s, name = %s, surName = %s, age = %s", person.getId(), person.getName(), person.getSurName(), person.getAge()));
        }
    }

    private static void clearPersons() {
        List<Person> persons = getAllPersons();
        for (Person person : persons) {
            deletePersonById(person.getId());
        }
    }

    public static List<Person> getAllPersons() {
        String querySelectAll = String.format("select * from persons");
        List<Person> persons = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, USER_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(querySelectAll);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Person person = new Person(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("surname"), resultSet.getInt("age"));
                persons.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return persons;
    }

    public static void insertPersons(List<Person> persons) {
        for (Person person : persons) {
            insertPerson(person);
        }
    }

    public static void insertPerson(Person person) {
        String queryInsert = "insert into persons values (?,?,?,?)";
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, USER_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(queryInsert);
            preparedStatement.setInt(1, person.getId());
            preparedStatement.setString(2, person.getName());
            preparedStatement.setString(3, person.getSurName());
            preparedStatement.setInt(4, person.getAge());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updatePersonById(int id, Person newPerson) {
        String queryUpdate = "update persons set name=?, surname=?, age=? where id=?";
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, USER_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(queryUpdate);
            preparedStatement.setString(1, newPerson.getName());
            preparedStatement.setString(2, newPerson.getSurName());
            preparedStatement.setInt(3, newPerson.getAge());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deletePersonById(int id) {
        String queryDelete = "delete from persons where id=?";
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, USER_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(queryDelete);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}