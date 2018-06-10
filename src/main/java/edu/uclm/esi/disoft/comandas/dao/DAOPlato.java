package edu.uclm.esi.disoft.comandas.dao;

import java.util.concurrent.ConcurrentHashMap;

import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import edu.uclm.esi.disoft.comandas.dominio.Plato;

public class DAOPlato {

	public static ConcurrentHashMap<String, Plato> load(String idCategoria) {
		MongoCollection<BsonDocument> platos = MongoBroker.get().getCollection("Plato");
		ConcurrentHashMap<String, Plato> result=new ConcurrentHashMap<>();
		BsonDocument criterio=new BsonDocument();
		criterio.put("idCategoria", new BsonObjectId(new ObjectId(idCategoria)));
		MongoCursor<BsonDocument> fiPlatos = platos.find(criterio).iterator();
		while (fiPlatos.hasNext()) {
			BsonDocument bsonPlato = fiPlatos.next();
			String _id = bsonPlato.getObjectId("_id").getValue().toHexString();
			String nombre=bsonPlato.getString("nombre").getValue();
			double precio=bsonPlato.getDouble("precio").getValue();
			Plato plato=new Plato(_id, nombre, precio);
			result.put(_id, plato);
		}
		return result;
	}

}
