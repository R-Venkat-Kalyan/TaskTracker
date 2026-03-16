package com.tracker.tasktracker.service;

import com.tracker.tasktracker.entity.FeedBackEntity;
import com.tracker.tasktracker.repository.FeedBackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedBackService {

    private final FeedBackRepository feedBackRepository;

    public void saveFeedBack(FeedBackEntity feedBack){
        feedBackRepository.save(feedBack);
    }
}
