package com.tracker.tasktracker.repository;

import com.tracker.tasktracker.entity.MilestoneLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MilestoneLogRepository extends MongoRepository<MilestoneLogEntity, String> {

//    List<MilestoneLogEntity> findByUserId(String userId);

    List<MilestoneLogEntity> findByUserIdAndDateBetweenOrderByDateAsc(
            String userId,
            LocalDate start,
            LocalDate end
    );

    // SUM story points
    @Query(value = "{userId:?0, date: {$gte:?1, $lte:?2}}", fields="{storyPoint:1}")
    Iterable<MilestoneLogEntity> findStoryPoints(String userId, LocalDate start, LocalDate end);

    // SUM effort hours
    @Query(value = "{userId:?0, date: {$gte:?1, $lte:?2}}", fields="{effortHours:1}")
    Iterable<MilestoneLogEntity> findEffortHours(String userId, LocalDate start, LocalDate end);

    // count distinct working days
    @Query(value = "{userId:?0, date: {$gte:?1, $lte:?2}}", fields="{date:1}")
    Iterable<MilestoneLogEntity> findDates(String userId, LocalDate start, LocalDate end);

    // Manager
    List<MilestoneLogEntity> findByDateBetweenOrderByEmpNameAscDateAsc(LocalDate start, LocalDate end);

}
