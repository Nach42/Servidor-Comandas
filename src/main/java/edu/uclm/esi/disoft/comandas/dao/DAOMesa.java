package edu.uclm.esi.disoft.comandas.dao;

import java.util.concurrent.ConcurrentHashMap;

import org.bson.BsonDocument;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import edu.uclm.esi.disoft.comandas.dominio.Mesa;

public class DAOMesa {

	public static ConcurrentHashMap<Integer, Mesa> load() {
		MongoCollection<BsonDocument> mesas = MongoBroker.get().getCollection("Mesa");
		ConcurrentHashMap<Integer, Mesa> result=new ConcurrentHashMap<>();
		MongoCursor<BsonDocument> fiMesas = mesas.find().iterator();
		while (fiMesas.hasNext()) {
			BsonDocument bsonMesa = fiMesas.next();
			int _id=(int) bsonMesa.getDouble("_id").getValue();
			Mesa mesa=new Mesa(_id);
			result.put(_id, mesa);
		}
		return result;
	}

}
