package org.example.sem10_dz.api;

import lombok.Data;
import org.example.sem10_dz.JUnitSpringBootBase;
import org.example.sem10_dz.model.Issue;
import org.example.sem10_dz.model.Reader;
import org.example.sem10_dz.repository.IssueRepository;
import org.example.sem10_dz.repository.ReaderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ReaderControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    IssueRepository issueRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Data
    static class JUnitReaderResponse {
        private Long id;
        private String name;
    }

    @Test
    void testGetReaderByIdSuccess() {
        Reader expected = readerRepository.save(new Reader("Семен"));

        ReaderControllerTest.JUnitReaderResponse responseBody = webTestClient.get()
                .uri("/reader/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ReaderControllerTest.JUnitReaderResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getName(), responseBody.getName());
    }

    @Test
    void testGetReaderByIdNotFound() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from readers", Long.class);

        webTestClient.get()
                .uri("/reader/" + maxId + 1)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testAddReader() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from readers", Long.class);

        ReaderControllerTest.JUnitReaderResponse request = new ReaderControllerTest.JUnitReaderResponse();
        request.setId(maxId + 1L);
        request.setName("Василий");

        ReaderControllerTest.JUnitReaderResponse responseBody = webTestClient.post()
                .uri("/reader")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ReaderControllerTest.JUnitReaderResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());
        Assertions.assertEquals(request, responseBody);
        Assertions.assertTrue(readerRepository.findById(request.getId()).isPresent());
    }

    @Test
    void testDeleteReader() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from books", Long.class);
        Random rnd = new Random();
        ReaderControllerTest.JUnitReaderResponse request = new ReaderControllerTest.JUnitReaderResponse();
        request.setId(rnd.nextLong(1L, maxId + 1L));

        webTestClient.delete()
                .uri("/reader/" + request.getId())
                .exchange()
                .expectStatus().isNoContent();

        Assertions.assertFalse(readerRepository.findById(request.getId()).isPresent());
    }

    @Test
    void testGetAllIssues() {
        Reader given = readerRepository.save(new Reader("Артем"));
        issueRepository.saveAll(List.of(
                new Issue(1L, given.getId()),
                new Issue(2L, given.getId())
        ));

        List<Issue> expected = issueRepository.findAll().stream()
                .filter(it -> Objects.equals(it.getReaderId(), given.getId()))
                .toList();

        List<IssueControllerTest.JUnitIssueResponse> responseBody = webTestClient.get()
                .uri("/reader/" + given.getId() + "/issue")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<IssueControllerTest.JUnitIssueResponse>>() {
                })
                .returnResult().getResponseBody();

        Assertions.assertEquals(expected.size(), responseBody.size());
        for (IssueControllerTest.JUnitIssueResponse issueResponse : responseBody) {
            boolean found = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), issueResponse.getId()))
                    .anyMatch(it -> Objects.equals(it.getBookId(), issueResponse.getBookId()));
            Assertions.assertTrue(found);
        }
    }
}
