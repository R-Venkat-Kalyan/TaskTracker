package com.tracker.tasktracker.repository;

import com.tracker.tasktracker.entity.LeaveEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends MongoRepository<LeaveEntity, String> {

    List<LeaveEntity> findByUserId(String userId);

}
