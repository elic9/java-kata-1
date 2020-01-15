package org.echocat.kata.java.part1.model;

import java.util.Collection;

public abstract class Publication {

    private final String title;

    private final String isbn;

    private final Collection<Author> authors;

    public Publication(String title, String isbn, Collection<Author> authors) {
        this.title = title;
        this.isbn = isbn;
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public Collection<Author> getAuthors() {
        return authors;
    }

    protected String formatAuthors() {
        StringBuilder sb = new StringBuilder();
        authors.forEach(a -> sb.append("\t\t").append(a.toString()).append("\n"));
        return sb.toString();
    }
}
