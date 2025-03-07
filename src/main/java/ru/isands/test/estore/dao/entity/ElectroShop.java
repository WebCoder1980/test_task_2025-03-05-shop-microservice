package ru.isands.test.estore.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(ElectroShopPK.class)
@Table(name = "store_eshop")
public class ElectroShop {
	@Id
	@Column(name = "shopid", nullable = false)
	Long shopId;

	@Id
	@Column(name = "electroid", nullable = false)
	Long electroId;

	@Column(name = "quantity", nullable = false)
	int quantity;
}
