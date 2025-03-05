package ru.isands.test.estore.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EmployeeDTO {
    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private Date birthDate;
    private Long positionId;
    private Long shopId;
    private boolean gender;
}
