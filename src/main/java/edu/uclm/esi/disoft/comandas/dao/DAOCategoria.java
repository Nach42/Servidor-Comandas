package edu.uclm.esi.disoft.comandas.dao;

import java.util.concurrent.ConcurrentHashMap;

import org.bson.BsonDocument;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import edu.uclm.esi.disoft.comandas.dominio.Categoria;

public class DAOCategoria {

	public static ConcurrentHashMap<String, Categoria> load() {
		MongoCollection<BsonDocument> categorias = MongoBroker.get().getCollection("Categoria");
		ConcurrentHashMap<String, Categoria> result=new ConcurrentHashMap<>();
		MongoCursor<BsonDocument> fiCategorias = categorias.find().iterator();
		while (fiCategorias.hasNext()) {
			BsonDocument bsonCategoria = fiCategorias.next();
			String _id = bsonCategoria.getObjectId("_id").getValue().toHexString();
			String nombre=bsonCategoria.getString("nombre").getValue();
			Categoria categoria=new Categoria(_id, nombre);
			result.put(_id, categoria);
		}
		return result;
	}

}
