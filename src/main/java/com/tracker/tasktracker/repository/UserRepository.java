package com.tracker.tasktracker.repository;

import com.tracker.tasktracker.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmpId(String empId);
}
