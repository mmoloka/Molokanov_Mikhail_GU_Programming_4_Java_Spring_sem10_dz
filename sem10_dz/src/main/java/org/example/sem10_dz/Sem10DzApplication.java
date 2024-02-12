package org.example.sem10_dz;

import org.example.sem10_dz.repository.BookRepository;
import org.example.sem10_dz.repository.IssueRepository;
import org.example.sem10_dz.repository.ReaderRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Sem10DzApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Sem10DzApplication.class, args);

        BookRepository bookRepository = context.getBean(BookRepository.class);
        ReaderRepository readerRepository = context.getBean(ReaderRepository.class);
        IssueRepository issueRepository = context.getBean(IssueRepository.class);

        System.out.println(bookRepository.findAll());
        System.out.println(readerRepository.findAll());
        System.out.println(issueRepository.findAll());
    }

}
