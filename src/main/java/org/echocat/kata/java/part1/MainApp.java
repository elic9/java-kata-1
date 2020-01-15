package org.echocat.kata.java.part1;

import org.echocat.kata.java.part1.model.Publication;
import org.echocat.kata.java.part1.service.PublicationsService;

import java.util.Collection;

@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class MainApp {

    public static void main(String[] args) {
        PublicationsService publicationsService = new PublicationsService();

        System.out.println("*** PRINT ALL BOOKS/MAGAZINES ***\n");
        publicationsService.getAllPublications().forEach(System.out::println);

        System.out.println("\n*** PRINT FIND BY ISBN ***\n");
        System.out.println(publicationsService.findByIsbn("5554-5545-4518"));

        System.out.println("\n*** PRINT FIND BY ISBN (NOT FOUND) ***\n");
        System.out.println(publicationsService.findByIsbn("NOTEXISTS"));

        System.out.println("\n*** PRINT BOOKS/MAGAZINES BY AUTHOR EMAIL ***\n");
        Collection<Publication> byAuthor = publicationsService.findByAuthor("null-walter@echocat.org");
        byAuthor.forEach(System.out::println);

        System.out.println("\n*** PRINT BOOKS/MAGAZINES BY AUTHOR EMAIL (NOT FOUND) ***\n");
        Collection<Publication> byAuthorNotFound = publicationsService.findByAuthor("notfound@echocat.org");
        byAuthorNotFound.forEach(System.out::println);

        System.out.println("\n*** PRINT ALL SORTED BY TITLE ***\n");
        publicationsService.getAllSorted(PublicationsService.SORT_BY_TITLE).forEach(System.out::println);

    }
}
