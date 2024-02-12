package org.example.sem10_dz.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.sem10_dz.model.Issue;
import org.example.sem10_dz.repository.IssueRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final BookService bookService;
    private final ReaderService readerService;


    @PostConstruct
    public void generateData() {
        issueRepository.save(new Issue(1L, 1L));
        issueRepository.save(new Issue(3L, 2L));
        issueRepository.save(new Issue(2L, 3L));
    }

    public Issue addIssue(Issue issue) {
        return issueRepository.save(issue);
    }

    public Issue getIssueById(Long id) {
        return issueRepository.findById(id).orElseThrow();
    }

}
