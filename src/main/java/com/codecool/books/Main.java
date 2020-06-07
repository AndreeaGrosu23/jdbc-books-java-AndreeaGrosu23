package com.codecool.books;

import com.codecool.books.model.*;
import com.codecool.books.view.UserInterface;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        UserInterface ui = new UserInterface(System.in, System.out);
        new Main(ui).run();
    }

    UserInterface ui;
    AuthorDao authorDao;

    Main(UserInterface ui) {
        this.ui = ui;
    }

    private void run() throws SQLException {
        setup();

        boolean running = true;

        while (running) {
            ui.printTitle("Main Menu");
            ui.printOption('a', "Authors");
            ui.printOption('b', "Books");
            ui.printOption('q', "Quit");
            switch (ui.choice("abq")) {
                case 'a':
                    new AuthorManager(ui, authorDao).run();
                    break;
                case 'b':
                    ui.println("Not implemented yet!");
                    break;
                case 'q':
                    running = false;
                    break;
            }
        }
    }

    private void setup() throws SQLException {
        ui.printOption('i', "In-memory database");
        ui.printOption('j', "JDBC database");
        switch (ui.choice("ij")) {
            case 'i':
                ui.println("Using in-memory database");
                authorDao = new AuthorDaoInMemory();
                createInitialData();
                break;
            case 'j':
                ui.println("Using JDBC");
                DataSource dataSource = connect();
                authorDao = new AuthorDaoJDBC(dataSource);
                break;
        }
    }

    private DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setDatabaseName("books");
        dataSource.setUser("andreeagrosu");
        dataSource.setPassword("Kirsebaerfarget123>>");

        ui.println("Trying to connect...");
        dataSource.getConnection().close();
        ui.println("Connection OK");

        return dataSource;
    }


    private void createInitialData() {
        ui.println("Creating initial data");

        Author author1 = new Author("J.R.R.", "Tolkien", Date.valueOf("1982-01-03"));
        Author author2 = new Author("Douglas", "Adams", Date.valueOf("1952-03-11"));
        Author author3 = new Author("George R. R.", "Martin", Date.valueOf("1948-09-20"));
        Author author4 = new Author("Frank", "Herbert", Date.valueOf("1920-10-08"));

        authorDao.add(author1);
        authorDao.add(author2);
        authorDao.add(author3);
        authorDao.add(author4);

        /*
        bookDao.add(new Book(author1, "Hobbit"));
        bookDao.add(new Book(author1, "Lord of the Rings"));
        bookDao.add(new Book(author2, "Hitchhiker's Guide to the Galaxy"));
        bookDao.add(new Book(author3, "A Game of Thrones"));
        bookDao.add(new Book(author3, "Tuf Voyaging"));
        bookDao.add(new Book(author4, "Dune"));
         */
    }

}
