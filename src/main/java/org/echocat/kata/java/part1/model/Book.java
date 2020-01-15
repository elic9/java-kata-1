package org.echocat.kata.java.part1.model;

import java.util.Collection;

public class Book extends Publication {

    private String description;

    public Book(String title, String idbn, Collection<Author> authors, String description) {
        super(title, idbn, authors);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("Book: \n" +
                "\tIsbn: %s\n" +
                "\tTitle: %s\n" +
                "\tDescription: %s\n" +
                "\tAuthors:\n%s", getIsbn(), getTitle(), description, formatAuthors());
    }
}
