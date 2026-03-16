package com.tracker.tasktracker.service;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryDTO {

    private String userId;
    private String empId;
    private String fullName;

    private double totalStoryPoints;
    private int totalEffortHours;
    private int totalWorkingDays; // unique working days with any log
    private int totalLeaveDays;

}
