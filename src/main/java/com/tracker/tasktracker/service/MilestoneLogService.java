package com.tracker.tasktracker.service;

import com.tracker.tasktracker.entity.MilestoneLogEntity;
import com.tracker.tasktracker.repository.MilestoneLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MilestoneLogService {

    private final MilestoneLogRepository milestoneLogRepository;

    public void saveMilestoneData(MilestoneLogEntity data){
        milestoneLogRepository.save(data);
    }

    public List<MilestoneLogEntity> findByUserId(String userId){
        return milestoneLogRepository.findByUserId(userId);
    }

    public List<MilestoneLogEntity> getLogsForMonth(String userId, LocalDate start, LocalDate end) {
        return milestoneLogRepository.findByUserIdAndDateBetween(userId, start, end);
    }

    public MilestoneLogEntity getById(String id) {
        return milestoneLogRepository.findById(id).orElseThrow();
    }

    public void deleteLog(String id) {
        milestoneLogRepository.deleteById(id);
    }
}
