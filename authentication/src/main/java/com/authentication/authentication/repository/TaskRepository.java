package com.authentication.authentication.repository;

import com.authentication.authentication.models.RoleModel;
import com.authentication.authentication.models.TaskModel;
import com.authentication.authentication.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long> {

    public List<TaskModel> findAll();

    // Owner_Username = ค้นหา owner getUsername
    // Owner_Email = ค้นหา owner getEmail
    public List<TaskModel> findByOwner_UsernameOrOwner_Email(String username, String email);

    public List<TaskModel> findByOwner_UsernameOrOwner_EmailAndTaskNameContaining(
            String username,
            String email,
            String task_name);

    public List<TaskModel> findByOwner_UsernameOrOwner_EmailAndCompleted(String username,
                                                                         String email,
                                                                         Boolean completed);
}
