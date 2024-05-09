package com.example.api.model.test;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "possible_results")
public class PossibleResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id", nullable = false)
    private TestResult result;

    @Column(nullable = false)
    private int startValue;

    @Column(nullable = false)
    private int endValue;

    @Column(nullable = false)
    private String content;

}
