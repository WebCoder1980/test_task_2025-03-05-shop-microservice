package ru.isands.test.estore.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ElectroShopDTO {
    @NotNull
    private Long shopId;

    @NotNull
    private Long electroId;

    @NotNull
    private Integer quantity;
}
