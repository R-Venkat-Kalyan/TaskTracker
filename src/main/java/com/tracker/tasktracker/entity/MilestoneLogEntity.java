package com.tracker.tasktracker.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "daily_tracker_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneLogEntity {

    @Id
    private String id;

    private String userId;

    private String empName;
    private String empId;

    private String sprint;
    private String release;
    private LocalDate date;

    private String program;
    private String jiraType;
    private String priority;

    private String userStoryId;
    private String jiraId;

    private boolean productionDefect;

    private String effortCategory;
    private String description;

    private double storyPoint;
    private String bugRca;
    private int effortHours;

    private String status;

    @CreatedDate
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate updatedAt;
}
