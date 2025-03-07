package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dto.ErrorDTO;
import ru.isands.test.estore.dto.ShopDTO;
import ru.isands.test.estore.service.ShopService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@Tag(name = "Shop", description = "Сервис для выполнения операций над магазинами")
@RequestMapping("/estore/api/shop")
public class ShopController {
	private final ShopService shopService;

	public ShopController(ShopService shopService) {
		this.shopService = shopService;
	}

	@PostMapping
	@Operation(summary = "Добавить магазин", responses = {
			@ApiResponse(responseCode = "200", description = "Магазин добавлен"),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<ShopDTO> add(@RequestBody ShopDTO shopDTO) {
		return ResponseEntity.ok(shopService.add(shopDTO));
	}

	@GetMapping
	@Operation(summary = "Получить все магазины", parameters = {
			@Parameter(name = "start", description = "Номер первого в результате магазина", schema = @Schema(type = "integer", defaultValue = "0")),
			@Parameter(name = "limit", description = "Максимальное колличество магазинов в результате", schema = @Schema(type = "integer", defaultValue = "1000000"))
			}, responses = {
			@ApiResponse(responseCode = "200", description = "Список магазинов"),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<List<ShopDTO>> getAll(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "limit", defaultValue = "1000000") int limit) {
		return ResponseEntity.ok(shopService.getAll(start, limit));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Получить магазин по ID", responses = {
			@ApiResponse(responseCode = "200", description = "Информация о магазине"),
			@ApiResponse(responseCode = "404", description = "Магазин не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
	})
	public ResponseEntity<ShopDTO> getById(@PathVariable Long id) {
		return ResponseEntity.ok(shopService.getById(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Обновить информацию о магазине", responses = {
			@ApiResponse(responseCode = "200", description = "Магазин обновлен"),
			@ApiResponse(responseCode = "404", description = "Магазин не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<ShopDTO> update(@PathVariable Long id, @RequestBody ShopDTO shopDTO) {
		return ResponseEntity.ok(shopService.update(id, shopDTO));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Удалить магазин", responses = {
			@ApiResponse(responseCode = "204", description = "Магазин удален"),
			@ApiResponse(responseCode = "404", description = "Магазин не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		shopService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/upload-csv")
	@Operation(summary = "Загрузить магазины из CSV", responses = {
			@ApiResponse(responseCode = "200", description = "Магазин успешно загружены"),
			@ApiResponse(responseCode = "404", description = "Магазин не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пожалуйста, загрузите корректный CSV файл.");
		}

		try {
			shopService.processCSVFile(file);
			return ResponseEntity.ok("Данные магазинов успешно загружены.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обработке файла.");
		}
	}
}
