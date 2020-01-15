package org.echocat.kata.java.part1.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.echocat.kata.java.part1.model.Author;
import org.echocat.kata.java.part1.model.Book;
import org.echocat.kata.java.part1.model.Magazine;
import org.echocat.kata.java.part1.model.Publication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PublicationsService {

    public final static Comparator<Publication> SORT_BY_TITLE = Comparator.comparing(Publication::getTitle);

    private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final static Function<CSVRecord, Author> AUTHORS_RECORDS_READER =
            r -> new Author(r.get(0), r.get(1), r.get(2));

    private final static BiFunction<CSVRecord, Map<String, Author>, Book> BOOKS_RECORDS_READER =
            (b, m) -> new Book(b.get(0), b.get(1), PublicationsService.collectAuthors(b.get(2), m), b.get(3));

    private final static BiFunction<CSVRecord, Map<String, Author>, Magazine> MAGAZINES_RECORDS_READER =
            (p, m) -> new Magazine(p.get(0), p.get(1), PublicationsService.collectAuthors(p.get(2), m), LocalDate.parse(p.get(3), DATE_FORMATTER));

    private static Collection<Author> collectAuthors(String authors, Map<String, Author> authorsMap) {
        if (Objects.isNull(authors)) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(authors.split(",")).stream().map(e -> authorsMap.get(e)).collect(Collectors.toList());
        }
    }

    private static final BiPredicate<Publication, String> FIND_BY_AUTHOR_PREDICATE =
            (p, e) -> p.getAuthors()
                    .stream()
                    .anyMatch(a -> a.getEmail().equals(e));

    private final Collection<Publication> publications;

    private final Map<String, Publication> mapPublicationsById;

    public PublicationsService() {
        Map<String, Author> authorMap = parseAuthors();
        this.publications = parsePublications(authorMap);
        this.mapPublicationsById= this.publications.stream().collect(Collectors.toMap(Publication::getIsbn, Function.identity()));
    }

    public Collection<Publication> getAllPublications() {
        return publications;
    }

    public Collection<Publication> getAllSorted(Comparator<Publication> comparator) {
        return this.publications.stream().sorted(comparator).collect(Collectors.toList());
    }

    public Optional<Publication> findByIsbn(String isbn) {
        return Optional.ofNullable(this.mapPublicationsById.getOrDefault(isbn, null));
    }

    public Collection<Publication> findByAuthor(String authorEmail) {
        return this.publications.stream()
                .filter(p -> FIND_BY_AUTHOR_PREDICATE.test(p, authorEmail))
                .collect(Collectors.toList());
    }

    private Map<String, Author> parseAuthors() {
        return parseCsv(AUTHORS_RECORDS_READER, "authors.csv", "email", "firstname", "lastname")
            .stream()
            .collect(Collectors.toMap(Author::getEmail, Function.identity()));
    }

    private Collection<Publication> parsePublications(Map<String, Author> authorsMap) {
        Function<CSVRecord, Book> bookCreators = csv -> BOOKS_RECORDS_READER.apply(csv, authorsMap);
        Collection<Publication> publications = new ArrayList<>(parseCsv(bookCreators, "books.csv", "title", "isbn", "authors", "description"));

        Function<CSVRecord, Magazine> magazineCreator = csv -> MAGAZINES_RECORDS_READER.apply(csv, authorsMap);
        publications.addAll(parseCsv(magazineCreator, "magazines.csv", "title", "isbn", "authors", "publishedAt"));

        return publications;
    }

    private <R> Collection<R> parseCsv(Function<CSVRecord, R> recordCreator, String resource, String... headers) {
        try (Reader reader =
                     new InputStreamReader(getClass().getResourceAsStream(String.format("/org/echocat/kata/java/part1/data/%s", resource)))) {
            CSVParser records = CSVFormat.newFormat(';')
                    .withHeader(headers)
                    .withFirstRecordAsHeader()
                    .parse(reader);

            return records.getRecords().stream().map(recordCreator).collect(Collectors.toList());
        }
        catch (IOException ioe) {
            System.out.println("LOG: could not parse CSV due to IOException " + ioe.getMessage());
            return Collections.emptyList();
        }
    }
}
