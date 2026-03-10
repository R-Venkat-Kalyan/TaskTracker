package com.tracker.tasktracker.service;

import com.tracker.tasktracker.entity.LeaveEntity;
import com.tracker.tasktracker.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRepository leaveRepository;

    public void saveLeave(LeaveEntity leave) {
        leaveRepository.save(leave);
    }

    public List<LeaveEntity> getLeavesByUserId(String userId) {
        return leaveRepository.findByUserId(userId);
    }

    public List<LeaveEntity> getAllLeaves() {
        return leaveRepository.findAll();
    }

    public List<LeaveEntity> getLeavesForMonth(String userId, LocalDate start, LocalDate end) {
        return leaveRepository.findByUserIdAndStartDateBetween(userId, start, end);
    }

    public void deleteLeave(String id) {
        leaveRepository.deleteById(id);
    }
}
