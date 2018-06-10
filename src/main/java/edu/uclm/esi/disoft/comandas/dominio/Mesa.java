package edu.uclm.esi.disoft.comandas.dominio;

import org.json.JSONObject;

import edu.uclm.esi.disoft.comandas.etiquetas.BSONable;
import edu.uclm.esi.disoft.comandas.etiquetas.JSONable;

@BSONable
public class Mesa {
	@JSONable
	private int _id;
	private String estado;
	private Comanda comandaActual;

	public Mesa() {}
	
	public Mesa(int id) {
		this._id=id;
	}
	
	public Comanda getComandaActual() {
		return comandaActual;
	}

	public JSONObject toJSONObject() {
		JSONObject jso=new JSONObject();
		jso.put("_id", this._id);
		jso.put("estado", comandaActual==null ? "Libre" : "Ocupada");
		return jso;
	}
	
	public void setPrecioComanda() {
		comandaActual.calcularPrecio();
	}
	
	public boolean estaLibre() {
		return comandaActual==null;
	}

	public void abrir() throws Exception {
		if (comandaActual!=null)
			throw new Exception("La mesa ya está abierta. Elige otra");
		comandaActual=new Comanda(this._id);
	}

	public void cerrar() throws Exception {
		if (comandaActual==null)
			throw new Exception("La mesa ya está cerrada");
		comandaActual.cerrar();
		comandaActual=null;
	}

	public PlatoPedido addToComanda(Plato plato, int unidades) {
		return this.comandaActual.add(plato, unidades);
	}

	public JSONObject estado() {
		JSONObject jso=new JSONObject();
		jso.put("_id", this._id);
		if (comandaActual==null) 
			jso.put("estado", "Libre");
		else {
			jso.put("comanda", this.comandaActual.toJSONObject());
		}
		return jso;
	}
}
