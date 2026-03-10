package com.tracker.tasktracker.repository;

import com.tracker.tasktracker.entity.MilestoneLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MilestoneLogRepository extends MongoRepository<MilestoneLogEntity, String> {

    List<MilestoneLogEntity> findByUserId(String userId);

    List<MilestoneLogEntity> findByUserIdAndDateBetween(
            String userId,
            LocalDate start,
            LocalDate end
    );
}
