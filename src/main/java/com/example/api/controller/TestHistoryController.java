package com.example.api.controller;

import com.example.api.service.TestHistoryManagementService;
import com.example.api.service.user.UserAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class TestHistoryController {

    @Autowired
    private TestHistoryManagementService testHistoryManagementService;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @GetMapping("/histories")
    public ResponseEntity<?> getAllTestHistory(@RequestHeader("Authorization") String bearerToken) {
        log.info("Get test histories");
        ResponseEntity<?> responseEntity = userAuthenticationService.validateUserAuthenticationBearerToken(bearerToken);
        if (responseEntity.getStatusCode().isError()) return responseEntity;

        Long userId = userAuthenticationService.getUserDetailsFromSecurityContext().getUser().getId();
        return ResponseEntity.ok().body(testHistoryManagementService.getTestHistoriesByUserId(userId));
    }

    @GetMapping("/history/{testHistoryId}")
    public ResponseEntity<?> getTestHistoryById(@RequestHeader("Authorization") String bearerToken,
                                            @PathVariable Long testHistoryId) {
        log.info("Get test history by id: {}", testHistoryId);
        ResponseEntity<?> responseEntity = userAuthenticationService.validateUserAuthenticationBearerToken(bearerToken);
        if (responseEntity.getStatusCode().isError()) return responseEntity;

        return ResponseEntity.ok().body(testHistoryManagementService.getTestHistoryById(testHistoryId));
    }
}
