package ru.isands.test.estore.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeDTO {
    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private LocalDate birthDate;
    private Long positionId;
    private Long shopId;
    private boolean gender;
}
