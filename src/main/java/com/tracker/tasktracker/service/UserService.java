package com.tracker.tasktracker.service;

import com.tracker.tasktracker.entity.UserEntity;
import com.tracker.tasktracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(UserEntity user){
        userRepository.save(user);
    }

    public List<UserEntity> findAllUsers(){
        return userRepository.findAll();
    }

    public UserEntity findUserById(String id){
        return  userRepository.findById(id).orElse(null);
    }

    public void deleteUserById(String id){
        userRepository.deleteById(id);
    }

    public boolean userExists(String email, String empId){

        return userRepository.existsByEmail(email) ||
                userRepository.existsByEmpId(empId);
    }

    public long countByRoleIgnoreCase(String role){
        return userRepository.countByRoleIgnoreCase(role);
    }

    public List<UserEntity> findEmployees() {
        return userRepository.findByRoleIgnoreCase("employee");
    }

}
