package com.authentication.authentication.dto;

import lombok.Data;

@Data
public class TaskModelDto {

    private Long owner_id;
    private Long task_id;
    private String task_name;
    private String desc;
    private Boolean completed;
}

