package com.codecool.books.model;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDaoJDBC implements AuthorDao {
    private DataSource dataSource;

    public AuthorDaoJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Author author) {
        Connection connection = null;

        try {
            connection = dataSource.getConnection();

            String query = "INSERT INTO author (first_name, last_name, birth_date) " +
                            "VALUES (?, ?, ?);";

            PreparedStatement preparedStatement= connection.prepareStatement(query);

            String firstName= author.getFirstName();
            String lastName=author.getLastName();
            Date birthDate=author.getBirthDate();

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2,lastName);
            preparedStatement.setDate(3, birthDate);

            preparedStatement.execute();

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Author author) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            String query = String.format("UPDATE author SET " +
                            "(first_name, last_name, birth_date) = ('%s','%s', '%s') WHERE author.id = '%d'",
                    author.getFirstName(), author.getLastName(), author.getBirthDate(), author.getId());

            statement.executeUpdate(query);

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Author get(int id) {

        Connection connection = null;
        Statement statement = null;
        Author author = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String query = String.format("SELECT * FROM author WHERE author.id = '%d';", id);

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date birthDate = resultSet.getDate("birth_date");

                author = new Author(firstName, lastName, birthDate);
                author.setId(id);

                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return author;
    }

    @Override
    public List<Author> getAll() {

        String query = "SELECT * FROM author;";

        List<Author> resultList = new ArrayList<>();

        try {

            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date birthDate = resultSet.getDate("birth_date");
                Author author = new Author(firstName, lastName, birthDate);
                resultList.add(author);
                author.setId(id);
            }

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
