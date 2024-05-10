package com.example.api.model.test;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "possible_answers",
        uniqueConstraints = @UniqueConstraint(columnNames = {"question_id", "answer_number"})
)
public class PossibleAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private TestQuestion question;

    @Column(nullable = false)
    private int number;

    @Column(nullable = false)
    private int value;

    @Column(nullable = false)
    private String content;

}
