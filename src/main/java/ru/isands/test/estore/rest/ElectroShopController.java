package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dto.ErrorDTO;
import ru.isands.test.estore.dto.ElectroShopDTO;
import ru.isands.test.estore.service.ElectroShopService;

import java.util.List;

@RestController
@Tag(name = "ElectroShop", description = "Сервис для выполнения операций над магазинами-товар")
@RequestMapping("/estore/api/electroshop")
public class ElectroShopController {
    private final ElectroShopService electroShopService;

    public ElectroShopController(ElectroShopService electroShopService) {
        this.electroShopService = electroShopService;
    }

    @PostMapping
    @Operation(summary = "Добавить магазин-товар", responses = {
            @ApiResponse(responseCode = "200", description = "Магазин-товар добавлен"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<ElectroShopDTO> add(@RequestBody ElectroShopDTO electroShopDTO) {
        return ResponseEntity.ok(electroShopService.add(electroShopDTO));
    }

    @GetMapping
    @Operation(summary = "Получить все магазин-товар", parameters = {
            @Parameter(name = "start", description = "Номер первого в результате магазин-товар", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "limit", description = "Максимальное колличество магазин-товар в результате", schema = @Schema(type = "integer", defaultValue = "1000000"))
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Список магазин-товар"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<List<ElectroShopDTO>> getAll(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "limit", defaultValue = "1000000") int limit) {
        return ResponseEntity.ok(electroShopService.getAll(start, limit));
    }

    @GetMapping("/id")
    @Operation(summary = "Получить магазин-товар по IDs", parameters = {
            @Parameter(name = "electroid"),
            @Parameter(name = "shopid")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Информация о магазин-товар"),
            @ApiResponse(responseCode = "404", description = "Магазин-товар не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
    })
    public ResponseEntity<ElectroShopDTO> getById(@RequestParam(value = "electroid") Long electroId, @RequestParam(value = "shopid") Long shopId) {
        return ResponseEntity.ok(electroShopService.getByElectroIdAndShopId(electroId, shopId));
    }

    @PutMapping
    @Operation(summary = "Обновить информацию о магазин-товаре", parameters = {
            @Parameter(name = "electroid"),
            @Parameter(name = "shopid")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Магазин-товар обновлен"),
            @ApiResponse(responseCode = "404", description = "Магазин-товар не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<ElectroShopDTO> update(@RequestParam(value = "electroid") Long electroId, @RequestParam(value = "shopid") Long shopId, @RequestBody ElectroShopDTO electroShopDTO) {
        return ResponseEntity.ok(electroShopService.update(electroId, shopId, electroShopDTO));
    }

    @DeleteMapping
    @Operation(summary = "Удалить магазин-товар", parameters = {
            @Parameter(name = "electroid"),
            @Parameter(name = "shopid")
    }, responses = {
            @ApiResponse(responseCode = "204", description = "Магазин-товар удален"),
            @ApiResponse(responseCode = "404", description = "Магазин-товар не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<Void> delete(@RequestParam(value = "electroid") Long electroId, @RequestParam(value = "shopid") Long shopId) {
        electroShopService.delete(electroId, shopId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload-csv")
    @Operation(summary = "Загрузить магазин-товары из CSV", responses = {
            @ApiResponse(responseCode = "200", description = "Магазин-товар успешно загружены"),
            @ApiResponse(responseCode = "404", description = "Магазин-товар не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пожалуйста, загрузите корректный CSV файл.");
        }

        try {
            electroShopService.processCSVFile(file);
            return ResponseEntity.ok("Данные магазин-товаров успешно загружены.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обработке файла.");
        }
    }
}
