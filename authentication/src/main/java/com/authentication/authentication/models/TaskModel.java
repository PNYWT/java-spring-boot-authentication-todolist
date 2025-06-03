package com.authentication.authentication.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "task")
public class TaskModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One - Many -> Task ต่อ หนึ่งคน, หนึ่งคนมีได้หลาย Task
//    @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id") // id ของ UserModel
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UserModel owner;

    @NotBlank(message = "taskName is require.")
    @Column(name = "task_name") // บอก SQL ว่า taskName คือ task_name ใน Database
    @JsonProperty("task_name")  // ใช้สำหรับเขียนออก
    // @JsonAlias({"task_name", "taskName"}) รองรับได้ตั้งคู่
    private String taskName;

    private String description;

    @NotNull(message = "Completed is required.")
    private Boolean completed;
}
