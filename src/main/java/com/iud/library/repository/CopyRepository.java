package com.iud.library.repository;

import com.iud.library.entity.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CopyRepository extends JpaRepository<Copy, Integer> {

    public List<Copy> findByBookId(Integer bookId);
}
