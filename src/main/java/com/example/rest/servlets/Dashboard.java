package com.example.rest.servlets;

import com.example.rest.beans.Book;
import com.example.rest.responses.BookResponse;
import com.example.rest.responses.ExceptionResponse;
import com.example.rest.responses.GetDashboardResponse;
import com.example.rest.responses.Response;
import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Dashboard", urlPatterns = {"/dashboard/*"})
public class Dashboard extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("In DashboardServlet GET");
        response.setContentType("application/json;charset=UTF-8");

        Gson gson = new Gson();

        try {
            List<Book> books = getBooksFromContext(request.getServletContext());
            GetDashboardResponse res = new GetDashboardResponse(books, 200);
            gson.toJson(res, response.getWriter());
        } catch (Exception ex) {
            ExceptionResponse exResponse = new ExceptionResponse();
            exResponse.setMessage(ex.getLocalizedMessage());
            exResponse.setStatus(500);
            response.setStatus(500);
            gson.toJson(exResponse, response.getWriter());
        }
        System.out.println("Out DashboardServlet GET");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        response.setContentType("application/json;charset=UTF-8");

        try {
            Book book = gson.fromJson(request.getReader(), Book.class);
            if(book.getTitle() == null || book.getAuthor() == null || book.getYear() == null)
                throw new Exception("Invalid data");

            book.setId(Book.getIdGenerator());
            addBookToContext(book, request.getServletContext());
            BookResponse bookResponse = new BookResponse(book, 201);
            gson.toJson(bookResponse, response.getWriter());
        } catch (Exception ex) {
            ExceptionResponse exResponse = new ExceptionResponse();
            exResponse.setMessage(ex.getLocalizedMessage());
            exResponse.setStatus(500);
            response.setStatus(500);
            gson.toJson(exResponse, response.getWriter());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        response.setContentType("application/json;charset=UTF-8");

        try {
            List<Book> books = getBooksFromContext(request.getServletContext());
            Integer index = Integer.valueOf(request.getPathInfo().substring(1));

            for(Book book : books) {
                if(book.getId().equals(index)) {
                    books.remove(book);
                    BookResponse bookResponse = new BookResponse(book, 200);
                    gson.toJson(bookResponse, response.getWriter());
                    return;
                }
            }

            Response bookResponse = new Response();
            bookResponse.setStatus(404);
            bookResponse.setMessage("Book cannot be found");
            gson.toJson(bookResponse, response.getWriter());
        } catch (Exception ex) {
            ExceptionResponse exResponse = new ExceptionResponse();
            exResponse.setMessage(ex.getLocalizedMessage());
            exResponse.setStatus(500);
            response.setStatus(500);
            gson.toJson(exResponse, response.getWriter());
        }
    }

    private List<Book> getBooksFromContext(ServletContext context) {
        return (List<Book>) context.getAttribute("books");
    }

    private void addBookToContext(Book book, ServletContext context) {
        List<Book> books = getBooksFromContext(context);
        books.add(book);
        context.setAttribute("books", books);
    }
}
