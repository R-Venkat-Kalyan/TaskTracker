package com.tracker.tasktracker.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "feedbacks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedBackEntity {

    @Id
    private String id;

    // From the form
    private String feedbackType;   // suggestion | issue | ui | feature | other
    private String message;        // feedback text

    // From session
    private String empName;        // mapped from session user
    private String empId;          // mapped from session user
    private String empMail;        // mapped from session user

    // Meta
    @CreatedDate
    private LocalDate createdAt;

}
