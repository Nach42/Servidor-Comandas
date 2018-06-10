package edu.uclm.esi.disoft.comandas.etiquetas;

import static org.junit.Assert.*;

import javax.lang.model.type.PrimitiveType;

import org.json.JSONObject;
import org.junit.Test;

import edu.uclm.esi.disoft.comandas.dominio.Comanda;
import edu.uclm.esi.disoft.comandas.dominio.Mesa;
import edu.uclm.esi.disoft.comandas.dominio.Plato;
import edu.uclm.esi.disoft.comandas.dominio.PlatoPedido;

public class TestJSONeador {

	Plato plato = new Plato("26", "Tortilla", 6.50);
	PlatoPedido platoPedido = new PlatoPedido(plato, 3);
	Comanda comanda = new Comanda();
	
	@Test
	public void test() {
		JSONObject jso = JSONeador.toJSONObject(platoPedido);
		System.out.println(jso.toString());
		String valorEsperado = "{\"unidades\":3,\"idPlato\":\"26\"}";
		System.out.println(valorEsperado);
		assertEquals(jso.toString(), valorEsperado);
	}
	
	/*@Test
	public void test2() {
		String a = platoPedido.toJSONObject().toString();
		String b = JSONeador.toJSONObject(platoPedido).toString();
		
		System.out.println(a);
		System.out.println(b);
		
		assertEquals(a, b);
		
	}
	
	@Test
	public void test3() {
		comanda.add(plato, 2);
		Plato tortilla = new Plato("2", "Tortilla", 6.5);
		comanda.add(tortilla, 1);
		
		String a = comanda.toJSONObject().toString();
		String b = JSONeador.toJSONObject(comanda).toString();
		
		System.out.println(a);
		System.out.println(b);
		
		assertEquals(a, b);
	}
	
	@Test
	public void test4() {
		Mesa mesa = new Mesa(1);
		try {
			mesa.abrir();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mesa.addToComanda(plato, 2);
		JSONObject jsoMesa = JSONeador.toJSONObject(mesa);
		jsoMesa.put("estado", mesa.estaLibre() ? "Libre" : "Ocupada");
		System.out.println(mesa.toJSONObject());
		System.out.println(jsoMesa);
		
		assertEquals(jsoMesa.toString(), mesa.toJSONObject().toString());
	}
	
	@Test
	public void test5() {
		Integer a = 1;
		
		if (a.getClass().isPrimitive()) 
			System.out.println("zon iguale");
		else
			System.out.println("No zon iguale");
		System.out.println(a.getClass().getTypeName());

		assertEquals(a.getClass(), Plato.class);
		
		
	}*/
	
}
