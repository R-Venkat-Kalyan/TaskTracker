package com.tracker.tasktracker.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "leaves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveEntity {

    @Id
    private String id;

    private String userId;
    private String empName;
    private String empId;

    private LocalDate startDate;
    private LocalDate endDate;

    private String leaveType;
    private int totalDays;
}
