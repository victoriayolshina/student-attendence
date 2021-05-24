package ru.isu.studentattendance.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.isu.studentattendance.domain.model.Group;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {

}
