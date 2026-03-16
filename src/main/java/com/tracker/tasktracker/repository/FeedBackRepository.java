package com.tracker.tasktracker.repository;

import com.tracker.tasktracker.entity.FeedBackEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackRepository extends MongoRepository<FeedBackEntity, String> {
}
