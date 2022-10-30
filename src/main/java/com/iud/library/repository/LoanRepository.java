package com.iud.library.repository;

import com.iud.library.entity.Copy;
import com.iud.library.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {

    public List<Copy> findByCopyId(Integer copyId);
}
