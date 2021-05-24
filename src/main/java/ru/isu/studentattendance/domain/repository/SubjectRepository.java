package ru.isu.studentattendance.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.isu.studentattendance.domain.model.Subject;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {

}
