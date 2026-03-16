package com.tracker.tasktracker.service;

import com.tracker.tasktracker.entity.LeaveEntity;
import com.tracker.tasktracker.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRepository leaveRepository;

    public void saveLeave(LeaveEntity leave) {
        leaveRepository.save(leave);
    }

    public boolean overlapsExisting(String userId, LocalDate start, LocalDate end) {
        return leaveRepository
                .existsByUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(userId, end, start);
    }

    public List<LeaveEntity> getLeavesByUserId(String userId) {
        return leaveRepository.findByUserId(userId);
    }

    public List<LeaveEntity> getAllLeaves() {
        return leaveRepository.findAll();
    }


    public List<LeaveEntity> getLeavesForMonth(String userId, LocalDate start, LocalDate end) {
        return leaveRepository
                .findByUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAsc(
                        userId, end, start
                );
    }

    public void deleteLeave(String id) {
        leaveRepository.deleteById(id);
    }

    public int getTotalPlannedLeaves(String month){

        YearMonth ym = YearMonth.parse(month);

        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<LeaveEntity> leaves =
                leaveRepository.findByStartDateBetweenOrderByEmpName(start,end);

        int total = 0;

        for(LeaveEntity leave : leaves){
            total += leave.getTotalDays();
        }

        return total;
    }


    public List<LeaveEntity> getAllLeavesForMonth(LocalDate start, LocalDate end) {
        return leaveRepository.findByStartDateBetweenOrderByEmpName(start,end);
    }
}
