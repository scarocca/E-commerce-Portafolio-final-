package cl.sergiocarocca.cita_ideal_cl.entity;

import java.math.BigDecimal;

public class ItemCarrito {
	private Plan plan;
	private int cantidad;

	public ItemCarrito() {
		super();
	}

	public ItemCarrito(Plan plan, int cantidad) {
		super();
		this.plan = plan;
		this.cantidad = cantidad;
	}
	
	

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getSubtotal() {
        return plan.getPrecioBase().multiply(new BigDecimal(cantidad));
    }
}
