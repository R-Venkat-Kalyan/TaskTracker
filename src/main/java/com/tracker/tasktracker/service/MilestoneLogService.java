package com.tracker.tasktracker.service;

import com.tracker.tasktracker.entity.MilestoneLogEntity;
import com.tracker.tasktracker.repository.MilestoneLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
