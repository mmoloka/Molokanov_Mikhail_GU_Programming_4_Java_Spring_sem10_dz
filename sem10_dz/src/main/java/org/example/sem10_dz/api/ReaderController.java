package org.example.sem10_dz.api;

import lombok.extern.slf4j.Slf4j;
import org.example.sem10_dz.model.Issue;
import org.example.sem10_dz.model.Reader;
import org.example.sem10_dz.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/reader")
public class ReaderController {
    private final ReaderService readerService;

    @Autowired
    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reader> getReaderById(@PathVariable long id) {
        final Reader reader;
        try {
            reader = readerService.getReaderById(id);
        } catch (NoSuchElementException e) {
            log.info(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reader, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable long id) {
        try {
            readerService.deleteReaderById(id);
        } catch (NoSuchElementException e) {
            log.info(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Reader> addReader(@RequestBody Reader reader) {
        log.info("Добавлен читатель:  name = {}", reader.getName());
        return new ResponseEntity<>(readerService.addReader(reader), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/issue")
    public ResponseEntity<List<Issue>> getAllIssues(@PathVariable long id) {
        try {
            readerService.getReaderById(id);
        } catch (NoSuchElementException e) {
            log.info(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(readerService.getAllIssues(id), HttpStatus.OK);
    }
}
