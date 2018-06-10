package edu.uclm.esi.disoft.comandas.etiquetas;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.impl.execchain.MainClientExec;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonObjectId;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.types.ObjectId;
import org.json.JSONArray;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import edu.uclm.esi.disoft.comandas.dao.DAOMesa;
import edu.uclm.esi.disoft.comandas.dao.MongoBroker;
import edu.uclm.esi.disoft.comandas.dominio.Categoria;
import edu.uclm.esi.disoft.comandas.dominio.Comanda;
import edu.uclm.esi.disoft.comandas.dominio.Mesa;
import edu.uclm.esi.disoft.comandas.dominio.Plato;
import edu.uclm.esi.disoft.comandas.dominio.PlatoPedido;

public class BSONeador {
	
	public static void insert(Object objeto) throws Exception{
		
		Class<?> clase = objeto.getClass();
		Field [] campos = clase.getDeclaredFields();
		BsonDocument bso = new BsonDocument();
		for(int i=0; i<campos.length; i++) {
			Field campo = campos[i];
			campo.setAccessible(true);
			Object valor = campo.get(objeto);
			if (valor==null)
				continue;
			BSONable anotacion = campo.getAnnotation(BSONable.class);
			if (anotacion == null)
				bso.append(campo.getName(), getBsonValue(valor));
			else {
				String nombreCampoAsociado = anotacion.campo();
				String nombreNuevo = anotacion.nombre();
				Field campoAsociado = valor.getClass().getDeclaredField(nombreCampoAsociado);
				campoAsociado.setAccessible(true);
				Object valorCampoAsociado = campoAsociado.get(valor);
				bso.append(nombreNuevo, getBsonValue(valorCampoAsociado));
			}
		}
		
		// 2. / 3.
		MongoCollection<BsonDocument> collection = MongoBroker.get().getCollection(clase.getSimpleName());
		System.out.println(collection.getClass().getName());
		collection.insertOne(bso);
	}
	
	private static BsonValue getBsonValue(Object valor) throws IllegalArgumentException, IllegalAccessException {
		Class <? extends Object>tipo = valor.getClass();
//		if(tipo == ObjectId.class)
//			return new BsonObjectId((ObjectId) valor);
		if(tipo==boolean.class || tipo== Boolean.class)
			return new BsonBoolean((boolean) valor);
		if(tipo==double.class || tipo== Double.class)
			return new BsonDouble((double) valor);
		if(tipo==String.class)
			return new BsonString(valor.toString());
		if(tipo==int.class || tipo==Integer.class)
			return new BsonInt32((int)valor);
		if(tipo==long.class || tipo==Long.class)
			return new BsonInt64((long)valor);
		if(tipo==Vector.class) {
			Vector vector = (Vector)valor;
			BsonArray bsoa = new BsonArray();
			for(int i=0;i<vector.size();i++) {
				BsonDocument bso = new BsonDocument();
				Object objeto = vector.get(i);
				Field[] campos = objeto.getClass().getDeclaredFields();
				for(int j=0;j<campos.length;j++) {
					Field campo = campos[j];
					campo.setAccessible(true);
					bso.put(campo.getName(), getBsonValue(campo.get(objeto)));
				}
				bsoa.add(bso);
			}
			return bsoa;
		}
		if(tipo.isAnnotationPresent(BSONable.class)) {
			Field[] campos = valor.getClass().getDeclaredFields();
			BsonDocument bso = new BsonDocument();
			for(int i=0; i<campos.length; i++) {
				Field campo = campos[i];
				campo.setAccessible(true);
				bso.put(campo.getName(), getBsonValue(campo.get(valor)));
			}
			return bso;
		}

		return null;
	}

	public static ConcurrentHashMap<Object,Object> load(Class<?> clase, Object... variables) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException{ //pasamos como parametro la clase que queremos cargar
		ConcurrentHashMap<Object,Object> result = new ConcurrentHashMap<>();
		MongoCollection<BsonDocument> collection = MongoBroker.get().getCollection(clase.getSimpleName());
		BsonDocument criterio=new BsonDocument();
		String buscador = "";
		String valor = "";
		if(variables.length > 0) {
			buscador = variables[0].toString();
			valor = variables[1].toString();
			criterio.put(buscador, new BsonObjectId(new ObjectId(valor)));
		}
		MongoCursor<BsonDocument> fi = collection.find(criterio).iterator();
		while(fi.hasNext()) {
			BsonDocument bso=fi.next();
			Object objeto = getObject(clase, bso);
			result.put(getId(bso), objeto);
		}
		return result;
	}

	private static Object getId(BsonDocument bso) {
		if(bso.get("_id").isString())
			return bso.get("_id").asString().getValue();
		if(bso.get("_id").isInt32())
			return bso.get("_id").asInt32().getValue();
		if(bso.get("_id").isObjectId()){
			return bso.get("_id").asObjectId().getValue().toHexString();
		}

		return null;
	}

	private static Object getObject(Class<?> clase, BsonDocument bso) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Object result = clase.newInstance(); // Mesa result = new Mesa() dependiendo de la clase que sea
		Iterator<String> nombresDeLosCampos = bso.keySet().iterator(); //cojo el nombre de los campos
		while(nombresDeLosCampos.hasNext()) {
			String nombreDeCampo = nombresDeLosCampos.next();
			Field campo = clase.getDeclaredField(nombreDeCampo);
			if(campo==null)
				continue;
			campo.setAccessible(true);
			BsonValue valorDelBson = bso.get(nombreDeCampo);
			set(campo, result, valorDelBson);
		}
		return result;
	}

	private static void set(Field campo, Object result, BsonValue valorDelBson) throws IllegalArgumentException, IllegalAccessException {
		if(valorDelBson.isString()) {
			campo.set(result, valorDelBson.asString().getValue()); //reult.campo="..."
			return;
		}
		if(valorDelBson.isInt32()){
			campo.set(result, valorDelBson.asInt32().getValue());
			return;
		}
		if(valorDelBson.isDouble()){
			campo.set(result, valorDelBson.asDouble().getValue());
			return;
		}
		if(valorDelBson.isObjectId()){
			campo.set(result, valorDelBson.asObjectId().getValue().toHexString());
			return;
		}
	}
	
	public static void update(Object objeto) throws Exception {
		Class<?> clase = objeto.getClass();
		Field [] campos = clase.getDeclaredFields();
		BsonDocument bso = new BsonDocument();
		for(int i=0; i<campos.length; i++) {
			Field campo = campos[i];
			campo.setAccessible(true);
			Object valor = campo.get(objeto);
			if (valor==null)
				continue;
			BSONable anotacion = campo.getAnnotation(BSONable.class);
			if (anotacion == null)
				bso.append(campo.getName(), getBsonValue(valor));
			else {
				String nombreCampoAsociado = anotacion.campo();
				String nombreNuevo = anotacion.nombre();
				Field campoAsociado = valor.getClass().getDeclaredField(nombreCampoAsociado);
				campoAsociado.setAccessible(true);
				Object valorCampoAsociado = campoAsociado.get(valor);
				bso.append(nombreNuevo, getBsonValue(valorCampoAsociado));
			}
		}
		bso.remove("_id");
		Field campoId = clase.getDeclaredField("_id");
		campoId.setAccessible(true);
		Object valorId = campoId.get(objeto);
		
		BsonDocument criterio = new BsonDocument();
		criterio.put("_id", getBsonValue(valorId));
		
		MongoCollection<BsonDocument> collection = MongoBroker.get().getCollection(clase.getSimpleName());
		collection.replaceOne(criterio, bso);
	}
	
	private static void delete(Object objeto) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException {
		Class<?> clase = objeto.getClass();
		MongoCollection<BsonDocument> collection = MongoBroker.get().getCollection(clase.getName());
		Field campoId = clase.getDeclaredField("_id");
		campoId.setAccessible(true);
		Object valorId = campoId.get(objeto);
		collection.deleteOne(new BsonDocument("_id", getBsonValue(valorId)));
		
		BSONable anotacion = clase.getAnnotation(BSONable.class);
		if(anotacion==null)
			return;
		String nombreClaseDependiente = anotacion.claseDependiente();
		Class<?> claseDependiente = Class.forName(nombreClaseDependiente);
		
		Vector<Field> camposDependientes = findCampoBSONable(claseDependiente, clase);
		if(camposDependientes==null)
			return;
		//@BSONable(campo = "_id", nombre = "idPlato", OnDeleteCascade = true)
		//private Plato plato;
		for(Field campoDependiente : camposDependientes) {
			//Coger la anotacion, ver si tiene OnDeleteCascade a true
			//Si la tiene, leer valor nombre de la anotacion(dará idPlato)
			//Ir a la coleccion PlatoPedido y hacer delete de todos los objetos cuyo idPlato se igual al _id del objeto Principal
		}
	}	
	
	private static Vector<Field> findCampoBSONable(Class<?> claseDependiente, Class<?> clase) {
		Vector<Field> result = null;
		Field[] camposClaseDependiente = claseDependiente.getDeclaredFields();
		for(int i=0;i<camposClaseDependiente.length;i++) {
			Field campo = camposClaseDependiente[i];
			if(campo.getType()==clase && campo.getAnnotation(BSONable.class)!=null) {
				if(result==null)
					result = new Vector<>();
				result.add(campo);
			}
		}
		
		return null;
	}

	public static void main(String[] args) throws Exception {
		Mesa mesa = new Mesa(1);
		mesa.abrir();
		Plato plato1 = new Plato("5af31ba683612bed7baeff97", "Ensalada de queso de cabra a la plancha", 6.6);
		Plato plato2 = new Plato("5af31ba583612bed7baeff95", "Ensalada variada", 5.4);
		plato1.setCategoria("5af31ba383612bed7baeff92");
		plato2.setCategoria("5af31ba383612bed7baeff92");
		
		mesa.addToComanda(plato1, 2);
		mesa.setPrecioComanda();
		insertar(mesa);
		
		mesa.addToComanda(plato2, 1);
		mesa.setPrecioComanda();
		insertar(mesa);
		
		/*ConcurrentHashMap<Object, Object> platos = load(PlatoPedido.class);
		Enumeration<Object> ePlatos = platos.elements();
		while (ePlatos.hasMoreElements()) {
			System.out.println(JSONeador.toJSONObject(ePlatos.nextElement()));
		}*/
	}

	private static void insertar(Mesa mesa) {
		try {
			if(mesa.getComandaActual().get_id() == null) {
				ObjectId id = new ObjectId();
				mesa.getComandaActual().set_id(id.toHexString());
				BSONeador.insert(mesa.getComandaActual());
			}else
				BSONeador.update(mesa.getComandaActual());
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}

	
}
