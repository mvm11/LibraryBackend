package com.iud.library.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "copies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Copy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "editionNumber", length = 30, nullable = false, unique = true)
    private String editionNumber;

    @Column(name="state")
    private String state;

    private boolean isLend;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
