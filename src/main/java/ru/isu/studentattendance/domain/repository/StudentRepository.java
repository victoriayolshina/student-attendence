package ru.isu.studentattendance.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.isu.studentattendance.domain.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
}
