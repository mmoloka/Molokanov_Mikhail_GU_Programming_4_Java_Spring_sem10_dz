package org.example.sem10_dz.service;

import jakarta.annotation.PostConstruct;
import org.example.sem10_dz.model.Issue;
import org.example.sem10_dz.model.Reader;
import org.example.sem10_dz.repository.IssueRepository;
import org.example.sem10_dz.repository.ReaderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ReaderService {
    private final ReaderRepository readerRepository;
    private final IssueRepository issueRepository;

    public ReaderService(ReaderRepository readerRepository, IssueRepository issueRepository) {
        this.readerRepository = readerRepository;
        this.issueRepository = issueRepository;
    }

    @PostConstruct
    public void generateData() {
        readerRepository.save(new Reader("Игорь"));
        readerRepository.save(new Reader("Иван"));
        readerRepository.save(new Reader("Петр"));
        readerRepository.save(new Reader("Ольга"));
    }

    public Reader getReaderById(Long id) {
        return readerRepository.findById(id).orElseThrow();
    }

    public void deleteReaderById(Long id) {
        Reader reader = readerRepository.findById(id).orElseThrow();
        readerRepository.delete(reader);
    }

    public Reader addReader(Reader reader) {
        return readerRepository.save(reader);
    }

    public List<Issue> getAllIssues(Long id) {
        readerRepository.findById(id).orElseThrow();
        return issueRepository.findAll().stream()
                .filter(it -> Objects.equals(it.getReaderId(), id))
                .toList();
    }
}
