package com.example.rest.responses;

import com.example.rest.beans.Book;

import java.util.List;

public class GetDashboardResponse {
    private List<Book> books;
    private int status;
    public GetDashboardResponse() {
    }
    public GetDashboardResponse(List<Book> books, int status) {
        this.books = books;
        this.status = status;
    }

    public List<Book> getBooks() {
        return books;
    }

    public int getStatus() {
        return status;
    }
}
