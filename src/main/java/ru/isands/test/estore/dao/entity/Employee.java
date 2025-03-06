package ru.isands.test.estore.dao.entity;


import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "store_employee")
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Идентификатор сотрудника
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "employee_counter")
	@TableGenerator(name = "employee_counter", pkColumnName = "name", pkColumnValue = "ru.isands.test.estore.dao.entity.Employee", table = "counter", valueColumnName = "currentid", allocationSize = 2)
	@Column(name = "id_", unique = true, nullable = false)
	Long id;
	
	/**
	 * Фамилия сотрудника
	 */
	@Column(name = "lastname", nullable = false, length = 100)
	String lastName;
	
	/**
	 * Имя сотрудника
	 */
	@Column(name = "firstname", nullable = false, length = 100)
	String firstName;
	
	/**
	 * Отчество сотрудника
	 */
	@Column(name = "patronymic", nullable = false, length = 100)
	String patronymic;
	
	/**
	 * Дата рождения сотрудника
	 */
	@Column(name = "birthdate", nullable = false)
	LocalDate birthDate;

	@ManyToOne
	@JoinColumn(name = "positionid", nullable = false)
	private PositionType position;

	@ManyToOne
	@JoinColumn(name = "shopid", nullable = false)
	private Shop shop;
	
	/**
	 * Пол сотрудника (true - мужской, false - женский)
	 */
	@Column(name = "gender", nullable = false)
	boolean gender;

}