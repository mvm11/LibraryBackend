package com.iud.library.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "publishers", uniqueConstraints = { @UniqueConstraint(columnNames = { "publisherName" })})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name="publisherName", nullable = false)
    private String publisherName;
}
