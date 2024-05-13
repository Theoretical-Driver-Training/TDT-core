package com.example.api.service;

import com.example.api.model.history.TestHistory;
import com.example.api.model.test.Test;
import com.example.api.model.user.User;
import com.example.api.service.user.TestEQService;
import com.example.api.service.user.UserAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TestProcessingService {

    @Autowired
    private TestHistoryManagementService testHistoryManagementService;
    @Autowired
    private UserAuthenticationService userAuthenticationService;
    @Autowired
    private TestManagementService testManagementService;
    @Autowired
    private TestEQService testEQService;

    @Transactional
    public void startTest(Long testId) {
        User user = userAuthenticationService.getUserDetailsFromSecurityContext().getUser();
        Test test = testManagementService.getTest(testId);

        log.info("Starting test: {}", test.getName());
        testHistoryManagementService.createTestHistory(user, test);
    }

    @Transactional
    public void endTest(Long testId) {
        User user = userAuthenticationService.getUserDetailsFromSecurityContext().getUser();
        TestHistory currentTestHistory = testHistoryManagementService.getCurrentTestHistory(user.getId(), testId);

        log.info("Ending test: {}", currentTestHistory.getTest().getId());
        testHistoryManagementService.updateTestHistory(testId, currentTestHistory);
        testEQService.calculateResults(currentTestHistory);
    }


}
