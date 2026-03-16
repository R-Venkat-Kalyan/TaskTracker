package com.tracker.tasktracker.service;

import com.tracker.tasktracker.entity.MilestoneLogEntity;
import com.tracker.tasktracker.repository.MilestoneLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MilestoneLogService {

    private final MilestoneLogRepository milestoneLogRepository;

    public void saveMilestoneData(MilestoneLogEntity data){
        milestoneLogRepository.save(data);
    }

    public List<MilestoneLogEntity> getLogsForMonth(String userId, LocalDate start, LocalDate end) {
        return milestoneLogRepository.findByUserIdAndDateBetweenOrderByDateAsc(userId, start, end);
    }

    public MilestoneLogEntity getById(String id) {
        return milestoneLogRepository.findById(id).orElseThrow();
    }

    public void deleteLog(String id) {
        milestoneLogRepository.deleteById(id);
    }

    public Iterable<MilestoneLogEntity> findStoryPoints(String userId, LocalDate start, LocalDate end) {
        return milestoneLogRepository.findStoryPoints(userId,start,end);
    }

    public Iterable<MilestoneLogEntity> findEffortHours(String userId, LocalDate start, LocalDate end) {
        return milestoneLogRepository.findEffortHours(userId,start,end);
    }

    public Iterable<MilestoneLogEntity> findDates(String userId, LocalDate start, LocalDate end) {
            return milestoneLogRepository.findDates(userId,start,end);
    }

    // Manager
    public double getTotalStoryPoints(String month){

        YearMonth ym = YearMonth.parse(month);

        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<MilestoneLogEntity> logs =
                milestoneLogRepository.findByDateBetweenOrderByEmpNameAscDateAsc(start,end);

        double total = 0;

        for(MilestoneLogEntity log : logs){
            total += log.getStoryPoint();
        }

        return total;
    }

    public int getTotalEffortHours(String month){

        YearMonth ym = YearMonth.parse(month);

        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<MilestoneLogEntity> logs =
                milestoneLogRepository.findByDateBetweenOrderByEmpNameAscDateAsc(start,end);

        int total = 0;

        for(MilestoneLogEntity log : logs){
            total += log.getEffortHours();
        }

        return total;
    }

    public int getTotalWorkingDays(String month){

        YearMonth ym = YearMonth.parse(month);

        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<MilestoneLogEntity> logs =
                milestoneLogRepository.findByDateBetweenOrderByEmpNameAscDateAsc(start,end);

        Set<String> unique = new HashSet<>();

        for(MilestoneLogEntity log : logs){

            String key = log.getUserId()+"-"+log.getDate();

            unique.add(key);
        }

        return unique.size();
    }

    public List<MilestoneLogEntity> getAllLogsForMonth(LocalDate start, LocalDate end) {

        return milestoneLogRepository.findByDateBetweenOrderByEmpNameAscDateAsc(start,end);
    }
}
