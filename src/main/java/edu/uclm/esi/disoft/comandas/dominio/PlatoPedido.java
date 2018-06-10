package edu.uclm.esi.disoft.comandas.dominio;

import org.json.JSONObject;

import edu.uclm.esi.disoft.comandas.etiquetas.BSONable;
import edu.uclm.esi.disoft.comandas.etiquetas.JSONable;

@BSONable
public class PlatoPedido {

	/*@JSONable
	private String _id;*/
	@JSONable(campo = "_id", nombre = "idPlato")
	@BSONable(campo = "_id", nombre = "idPlato", OnDeleteCascade = true)
	private Plato plato;
	@JSONable
	private int unidades;
	private boolean preparado;
	
	public PlatoPedido() {
	}
	
	public PlatoPedido(Plato plato, int unidades) {
		this.preparado=false;
		this.plato=plato;
		this.unidades=unidades;
	}
	
	public Plato getPlato() {
		return plato;
	}
	public int getUnidades() {
		return unidades;
	}
	
	public boolean getPreparado() {
		return preparado;
	}
	
	public void setPreparado(boolean preparado) {
		this.preparado=preparado;
	}

	public JSONObject toJSONObject() {
		JSONObject jso=this.plato.toJSONObject();
		jso.put("unidades", this.unidades);
		jso.put("preparado", this.preparado);
		return jso;
	}
}
