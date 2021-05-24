package ru.isu.studentattendance.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import ru.isu.studentattendance.domain.model.AutoUser;


@Repository
public interface AutoUserRepository extends JpaRepository<AutoUser, Long> {

    AutoUser findByUsername(String username);
}
