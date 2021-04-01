package com.example.rest.listeners;

import com.example.rest.beans.Book;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;

@WebListener
public class LibraryContextServletListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book(Book.getIdGenerator(),"Ogniem i Mieczem", "Henryk Sienkiewicz", 1884));
        books.add(new Book(Book.getIdGenerator(),"Potop", "Henryk Sienkiewicz", 1886));
        books.add((new Book(Book.getIdGenerator(),"Pan Wołodyjowski", "Henryk Sienkiewicz", 1888)));

        sce.getServletContext().setAttribute("books", books);
    }
}