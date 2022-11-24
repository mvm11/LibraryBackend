package com.iud.library.repository;

import com.iud.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    public List<Author> findByBookId(Integer bookId);
}
