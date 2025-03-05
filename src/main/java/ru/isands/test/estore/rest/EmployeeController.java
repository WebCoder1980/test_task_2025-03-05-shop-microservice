package ru.isands.test.estore.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isands.test.estore.dto.EmployeeDTO;
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
			@ApiResponse(description = "Сотрудник добавлен")
	})
	public ResponseEntity<EmployeeDTO> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
		return ResponseEntity.ok(employeeService.addEmployee(employeeDTO));
	}

	@GetMapping
	@Operation(summary = "Получить всех сотрудников", responses = {
			@ApiResponse(description = "Список сотрудников")
	})
	public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
		return ResponseEntity.ok(employeeService.getAllEmployees());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Получить сотрудника по ID", responses = {
			@ApiResponse(description = "Информация о сотруднике")
	})
	public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
		return ResponseEntity.ok(employeeService.getEmployeeById(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Обновить информацию о сотруднике", responses = {
			@ApiResponse(description = "Сотрудник обновлен")
	})
	public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
		return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Удалить сотрудника", responses = {
			@ApiResponse(description = "Сотрудник удален")
	})
	public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
		employeeService.deleteEmployee(id);
		return ResponseEntity.noContent().build();
	}
}
