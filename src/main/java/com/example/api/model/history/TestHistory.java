package com.example.api.model.history;

import com.example.api.model.test.Test;
import com.example.api.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "test_history")
public class TestHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestStatus status;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    private Long currentTestCursor;

    @OneToMany(mappedBy = "testHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestHistoryAnswer> answerList = new ArrayList<>();

    @OneToMany(mappedBy = "testHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestHistoryResult> resultList = new ArrayList<>();

}
