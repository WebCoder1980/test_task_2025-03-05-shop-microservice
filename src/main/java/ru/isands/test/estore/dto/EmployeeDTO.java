package ru.isands.test.estore.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeDTO {
    @NonNull
    private Long id;

    @NonNull
    private String lastName;

    @NonNull
    private String firstName;

    @NonNull
    private String patronymic;

    @NonNull
    private LocalDate birthDate;

    @NonNull
    private Long positionId;

    @NonNull
    private Long shopId;

    @NonNull
    private Boolean gender;
}
