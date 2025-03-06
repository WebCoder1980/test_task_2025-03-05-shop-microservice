package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dto.EmployeeDTO;
import ru.isands.test.estore.dto.ErrorDTO;
import ru.isands.test.estore.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@Tag(name = "Employee", description = "Сервис для выполнения операций над сотрудниками магазина")
@RequestMapping("/estore/api/employee")
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@PostMapping
	@Operation(summary = "Добавить сотрудника", responses = {
			@ApiResponse(responseCode = "200", description = "Сотрудник добавлен"),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<EmployeeDTO> add(@RequestBody EmployeeDTO employeeDTO) {
		return ResponseEntity.ok(employeeService.add(employeeDTO));
	}

	@GetMapping
	@Operation(summary = "Получить всех сотрудников", parameters = {
			@Parameter(name = "start", description = "Номер первого в результате сотрудника", schema = @Schema(type = "integer", defaultValue = "0")),
			@Parameter(name = "limit", description = "Максимальное колличество сотрудников в результате", schema = @Schema(type = "integer", defaultValue = "1000000"))
			}, responses = {
			@ApiResponse(responseCode = "200", description = "Список сотрудников"),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<List<EmployeeDTO>> getAll(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "limit", defaultValue = "1000000") int limit) {
		return ResponseEntity.ok(employeeService.getAll(start, limit));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Получить сотрудника по ID", responses = {
			@ApiResponse(responseCode = "200", description = "Информация о сотруднике"),
			@ApiResponse(responseCode = "404", description = "Сотрудник не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
	})
	public ResponseEntity<EmployeeDTO> getById(@PathVariable Long id) {
		return ResponseEntity.ok(employeeService.getById(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Обновить информацию о сотруднике", responses = {
			@ApiResponse(responseCode = "200", description = "Сотрудник обновлен"),
			@ApiResponse(responseCode = "404", description = "Сотрудник не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<EmployeeDTO> update(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
		return ResponseEntity.ok(employeeService.update(id, employeeDTO));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Удалить сотрудника", responses = {
			@ApiResponse(responseCode = "204", description = "Сотрудник удален"),
			@ApiResponse(responseCode = "404", description = "Сотрудник не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		employeeService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/upload-csv")
	@Operation(summary = "Загрузить сотрудников из CSV", responses = {
			@ApiResponse(responseCode = "200", description = "Сотрудники успешно загружены"),
			@ApiResponse(responseCode = "404", description = "Сотрудник не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пожалуйста, загрузите корректный CSV файл.");
		}

		try {
			employeeService.processCSVFile(file);
			return ResponseEntity.ok("Данные сотрудников успешно загружены.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обработке файла.");
		}
	}
}
