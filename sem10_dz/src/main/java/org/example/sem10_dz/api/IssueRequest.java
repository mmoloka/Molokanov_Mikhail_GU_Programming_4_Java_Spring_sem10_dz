package org.example.sem10_dz.api;

import lombok.Data;

@Data
public class IssueRequest {
    private Long readerId;
    private Long bookId;
}
