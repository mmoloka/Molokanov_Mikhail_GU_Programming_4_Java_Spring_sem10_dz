package org.example.sem10_dz.api;

import lombok.Data;
import org.example.sem10_dz.JUnitSpringBootBase;
import org.example.sem10_dz.model.Issue;
import org.example.sem10_dz.repository.IssueRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

public class IssueControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    IssueRepository issueRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Data
    static class JUnitIssueResponse {
        private Long id;
        private Long bookId;
        private Long readerId;
        private LocalDateTime timestamp;
    }

    @Test
    void testGetIssueByIdSuccess() {
        Issue expected = issueRepository.save(new Issue(4L, 4L));

        IssueControllerTest.JUnitIssueResponse responseBody = webTestClient.get()
                .uri("/issue/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(IssueControllerTest.JUnitIssueResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getBookId(), responseBody.getBookId());
        Assertions.assertEquals(expected.getReaderId(), responseBody.getReaderId());
    }

    @Test
    void testGetIssueByIdNotFound() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from issues", Long.class);

        webTestClient.get()
                .uri("/issue/" + maxId + 1)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testIssueBook() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from issues", Long.class);

        IssueControllerTest.JUnitIssueResponse request = new IssueControllerTest.JUnitIssueResponse();
        request.setId(maxId + 1L);
        request.setBookId(2L);
        request.setReaderId(2L);

        IssueControllerTest.JUnitIssueResponse responseBody = webTestClient.post()
                .uri("/issue")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(IssueControllerTest.JUnitIssueResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());
        Assertions.assertEquals(request.getId(), responseBody.getId());
        Assertions.assertEquals(request.getBookId(), responseBody.getBookId());
        Assertions.assertEquals(request.getReaderId(), responseBody.getReaderId());
        Assertions.assertTrue(issueRepository.findById(request.getId()).isPresent());
    }
}
