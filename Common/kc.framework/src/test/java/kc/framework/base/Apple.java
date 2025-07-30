package kc.framework.base;

import java.math.BigDecimal;

import lombok.Data;

@Data

public class Apple {
	private Integer id;
	private String name;
	private BigDecimal money;
	private Integer num;

	public Apple(Integer id, String name, BigDecimal money, Integer num) {
		this.id = id;
		this.name = name;
		this.money = money;
		this.num = num;
	}
}