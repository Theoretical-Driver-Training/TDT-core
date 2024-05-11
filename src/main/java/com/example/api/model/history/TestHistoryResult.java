package com.example.api.model.history;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "test_history_results")
public class TestHistoryResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_history_id", nullable = false)
    private TestHistory testHistory;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer value;

    @Column(nullable = false)
    private String description;

}
