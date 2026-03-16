package com.tracker.tasktracker.repository;

import com.tracker.tasktracker.entity.LeaveEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRepository extends MongoRepository<LeaveEntity, String> {

    List<LeaveEntity> findByUserId(String userId);

    List<LeaveEntity> findByUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAsc(
            String userId,
            LocalDate end,
            LocalDate start
    );

    boolean existsByUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String userId,
            LocalDate end,      // <= endOfNew
            LocalDate start     // >= startOfNew
    );

    List<LeaveEntity> findByStartDateBetweenOrderByEmpName(LocalDate start, LocalDate end);



}
