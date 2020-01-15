package org.echocat.kata.java.part1.model;

import java.time.LocalDate;
import java.util.Collection;

public class Magazine extends Publication {

    private LocalDate publishedAt;

    public Magazine(String title, String idbn, Collection<Author> authors, LocalDate publishedAt) {
        super(title, idbn, authors);
        this.publishedAt = publishedAt;
    }

    public LocalDate getPublishedAt() {
        return publishedAt;
    }

    @Override
    public String toString() {
        return String.format("Magazine: \n" +
                "\tIsbn: %s\n" +
                "\tTitle: %s\n" +
                "\tpublishedAt: %s\n" +
                "\tAuthors:\n%s", getIsbn(), getTitle(), publishedAt, formatAuthors());
    }
}
