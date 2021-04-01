package com.example.rest.responses;

import com.example.rest.beans.Book;

public class BookResponse {
    private Book book;
    private int status;
    public BookResponse() {
    }
    public BookResponse(Book book, int status) {
        this.book = book;
        this.status = status;
    }

    public Book getBooks() {
        return book;
    }

    public int getStatus() {
        return status;
    }
}
