package com.iud.library.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date startDate;
    private Date finishDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_copy")
    private Copy copy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_user_id", nullable = false)
    private LibraryUser libraryUser;


}
