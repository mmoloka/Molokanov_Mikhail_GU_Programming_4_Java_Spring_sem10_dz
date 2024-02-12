package org.example.sem10_dz.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "issues")
@Data
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bookId")
    private Long bookId;

    @Column(name = "readerId")
    private Long readerId;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public Issue() {
    }

    public Issue(Long bookId, Long readerId) {
        this.bookId = bookId;
        this.readerId = readerId;
        this.timestamp = LocalDateTime.now();
    }
}
