package org.example.sem10_dz.api;

import lombok.Data;
import org.example.sem10_dz.JUnitSpringBootBase;
import org.example.sem10_dz.model.Book;
import org.example.sem10_dz.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Random;

public class BookControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Data
    static class JUnitBookResponse {
        private Long id;
        private String name;
    }

    @Test
    void testGetBookByIdSuccess() {
        Book expected = bookRepository.save(new Book("вий"));

        JUnitBookResponse responseBody = webTestClient.get()
                .uri("/book/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitBookResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getName(), responseBody.getName());
    }

    @Test
    void testGetBookByIdNotFound() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from books", Long.class);

        webTestClient.get()
                .uri("/book/" + maxId + 1)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testAddBook() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from books", Long.class);

        JUnitBookResponse request = new JUnitBookResponse();
        request.setId(maxId + 1L);
        request.setName("горе от ума");

        JUnitBookResponse responseBody = webTestClient.post()
                .uri("/book")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JUnitBookResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());
        Assertions.assertEquals(request, responseBody);
        Assertions.assertTrue(bookRepository.findById(request.getId()).isPresent());
    }

    @Test
    void testDeleteBook() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from books", Long.class);
        Random rnd = new Random();
        JUnitBookResponse request = new JUnitBookResponse();
        request.setId(rnd.nextLong(1L, maxId + 1L));

        webTestClient.delete()
                .uri("/book/" + request.getId())
                .exchange()
                .expectStatus().isNoContent();

        Assertions.assertFalse(bookRepository.findById(request.getId()).isPresent());
    }
}
