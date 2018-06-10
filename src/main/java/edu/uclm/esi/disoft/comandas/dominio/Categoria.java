package edu.uclm.esi.disoft.comandas.dominio;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.uclm.esi.disoft.comandas.dao.DAOPlato;
import edu.uclm.esi.disoft.comandas.etiquetas.BSONable;
import edu.uclm.esi.disoft.comandas.etiquetas.BSONeador;
@BSONable
public class Categoria {
	private String _id;
	private String nombre;
	private JSONArray jsaPlatos;
	private ConcurrentHashMap<Object, Object> platos;
	
	public Categoria() {}
	
	public Categoria(String _id, String nombre) {
		this._id=_id;
		this.nombre=nombre;
		
	}
	
	public void init() throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException {
		this.jsaPlatos=new JSONArray();
		this.platos=BSONeador.load(Plato.class, "idCategoria", this._id);
		Enumeration<Object> ePlatos = this.platos.elements();
		while (ePlatos.hasMoreElements()) {
			Plato plato = (Plato) ePlatos.nextElement();
			this.jsaPlatos.put(plato.toJSONObject());
		}
	}

	public Plato find(String idPlato) {
		Plato plato = (Plato) this.platos.get(idPlato);
		return plato;
	}
	
	public JSONArray getPlatos() {
		return jsaPlatos;
	}

	public JSONObject toJSONObject() {
		JSONObject jso=new JSONObject();
		jso.put("_id", this._id);
		jso.put("nombre", this.nombre);
		jso.put("platos", this.jsaPlatos);
		return jso;
	}
}
