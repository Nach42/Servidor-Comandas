package edu.uclm.esi.disoft.comandas.dominio;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.uclm.esi.disoft.comandas.etiquetas.JSONable;

public class Comanda {
	private String _id;
	@JSONable
	private int idMesa;
	@JSONable
	private long horaApertura, horaCierre;
	@JSONable
	private Vector<PlatoPedido> platos;
	@JSONable
	private double precio;
	
	public Comanda() {
	}
	public Comanda(int idMesa) {
		this.idMesa=idMesa;
		this.horaApertura=System.currentTimeMillis();
		this.platos=new Vector<>();
		this.precio=0.0;
	}
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}

	public void calcularPrecio() {
		double precio = 0.0;
		for(int i=0; i<this.platos.size() ;i++) {
			precio += (this.platos.get(i).getPlato().getPrecio() * this.platos.get(i).getUnidades());
		}
		this.precio = precio;
	}

	public void cerrar() {
		this.horaCierre=System.currentTimeMillis();
	}

	public PlatoPedido add(Plato plato, int unidades) {
		PlatoPedido platoPedido=new PlatoPedido(plato, unidades);
		this.platos.add(platoPedido);
		return platoPedido;
	}

	public JSONObject toJSONObject() {
		JSONObject jso=new JSONObject();
		jso.put("idMesa", this.idMesa);
		jso.put("horaApertura", Auxi.getHora(this.horaApertura));
		JSONArray platosPedidos=new JSONArray();
		for (PlatoPedido pp : this.platos)
			platosPedidos.put(pp.toJSONObject());
		jso.put("platos", platosPedidos);
		jso.put("precio", precio);
		return jso;
	}

	public Vector<PlatoPedido> getPlatos() {
		return platos;
	}

	public double getPrecio() {
		return precio;
	}
	public void setPlatos(Vector<PlatoPedido> platos2) {
		this.platos = platos2;
	}
}
