package com.authentication.authentication.services;

import com.authentication.authentication.dto.TaskModelDto;
import com.authentication.authentication.exception.components.TaskNotFoundException;
import com.authentication.authentication.models.TaskModel;
import com.authentication.authentication.models.UserModel;
import com.authentication.authentication.repository.TaskRepository;
import com.authentication.authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskService {

    private final ModelMapper modelMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(ModelMapper modelMapper, TaskRepository taskRepository, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        setupMapperModel();
    }

    private void setupMapperModel() {
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE).setFieldMatchingEnabled(true).setSkipNullEnabled(true).setAmbiguityIgnored(true);

        TypeMap<TaskModel, TaskModelDto> taskMapper = this.modelMapper.createTypeMap(TaskModel.class, TaskModelDto.class);
        taskMapper.addMappings(mapper -> {
            mapper.map(src -> src.getId(), TaskModelDto::setTask_id);
            mapper.map(src -> src.getOwner().getId(), TaskModelDto::setOwner_id);
            mapper.map(src -> src.getDescription(), TaskModelDto::setDesc);
        });
    }

    /// Create Task
    public TaskModelDto createTask(TaskModel taskModel, String usernameOrEmail) {
        Optional<UserModel> user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        taskModel.setOwner(user.get());

        TaskModel taskSaved = taskRepository.save(taskModel);
        TaskModelDto taskModelDto = this.modelMapper.map(taskSaved, TaskModelDto.class);
        return taskModelDto;
    }

    /// Find Task
    private List<TaskModelDto> mapTaskModelDto(List<TaskModel> listTaskModel) {
        List<TaskModelDto> myListTask = listTaskModel.stream().map(task -> modelMapper.map(task, TaskModelDto.class)).collect(Collectors.toList());
        return myListTask;
    }

    /// getFindAll
    public List<TaskModelDto> getFindAll() {
        return mapTaskModelDto(taskRepository.findAll());
    }

    /// getAllTaskUser
    public List<TaskModelDto> getAllTaskUser(String usernameOrEmail) {
        List<TaskModel> myTask = taskRepository.findByOwner_UsernameOrOwner_Email(usernameOrEmail, usernameOrEmail);
        List<TaskModelDto> myListTask = mapTaskModelDto(myTask);
        return myListTask;
    }

    /// getTaskByTaskName
    public List<TaskModelDto> getTaskByTaskName(String task_name, String usernameOrEmail) {
        List<TaskModel> myTask = taskRepository.findByOwner_UsernameOrOwner_EmailAndTaskNameContaining(usernameOrEmail, usernameOrEmail, task_name);
        List<TaskModelDto> myListTaskName = mapTaskModelDto(myTask);
        return myListTaskName;
    }

    /// findTaskModelDtoById
    public TaskModelDto findTaskModelDtoById(Long id, String usernameOrEmail) {
        return findTaskModelById(id, usernameOrEmail)
                .map(task -> modelMapper.map(task, TaskModelDto.class))
                .orElseThrow(() -> new TaskNotFoundException(id));
        /*
                return taskRepository.findById(id)
                .filter(task -> {
                    log.debug("task.owner.name = {}", task.getOwner().getName());
                    log.debug("task.owner.email = {}", task.getOwner().getEmail());
                    log.debug("usernameOrEmail = {}", usernameOrEmail);
                    return Objects.equals(task.getOwner().getName(), usernameOrEmail)
                            || Objects.equals(task.getOwner().getEmail(), usernameOrEmail);
                })
                .map(task -> modelMapper.map(task, TaskModelDto.class));
         */
    }

    /// findTaskModelById
    private Optional<TaskModel> findTaskModelById(Long id, String usernameOrEmail) {
        return taskRepository.findById(id).filter(task -> {
            log.debug("task.owner.name = {}", task.getOwner().getName());
            log.debug("task.owner.email = {}", task.getOwner().getEmail());
            log.debug("usernameOrEmail = {}", usernameOrEmail);
            return Objects.equals(task.getOwner().getName(), usernameOrEmail) || Objects.equals(task.getOwner().getEmail(), usernameOrEmail);
        });
    }

    /// findTaskByCompleted
    public List<TaskModelDto> findTaskByCompleted(Boolean completed, String usernameOrEmail) {
        List<TaskModel> myTaskStatus = taskRepository.findByOwner_UsernameOrOwner_EmailAndCompleted(usernameOrEmail, usernameOrEmail, completed);
        List<TaskModelDto> myListTaskStatus = mapTaskModelDto(myTaskStatus);
        return myListTaskStatus;
    }

    /// updateTask
    public TaskModelDto updateTask(TaskModelDto taskModelDto, String usernameOrEmail) {
        TaskModel task = findTaskModelById(taskModelDto.getTask_id(), usernameOrEmail)
                .orElseThrow(() -> new TaskNotFoundException(taskModelDto.getTask_id()));

        if (taskModelDto.getTask_name() != null) {
            task.setTaskName(taskModelDto.getTask_name());
        }
        if (taskModelDto.getDesc() != null) {
            task.setDescription(taskModelDto.getDesc());
        }
        if (taskModelDto.getCompleted() != null) {
            task.setCompleted(taskModelDto.getCompleted());
        }

        TaskModel taskSaved = taskRepository.save(task);
        return this.modelMapper.map(taskSaved, TaskModelDto.class);
    }

    public void deleteTask(Long taskId, String usernameOrEmail) {
        TaskModel task = findTaskModelById(taskId, usernameOrEmail)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        taskRepository.delete(task);
        /*
        ถ้า deleteById(id) จะเจอปัญหา owner_id
        แก้ด้วยการเอา owner_id ออกก่อน
        task.setOwner(null)
        taskRepository.save(task)
        taskRepository.deleteById(id);
        จะทำให้ลบได้
         */
    }
}
