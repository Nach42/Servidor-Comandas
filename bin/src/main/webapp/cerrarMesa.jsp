<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.*, edu.uclm.esi.disoft.comandas.dominio.Manager" %>

<%
	String p=request.getParameter("p");
	JSONObject jso=new JSONObject(p);
	int idMesa=jso.getInt("id");
	
	JSONObject respuesta=new JSONObject();
	try {
		Manager.get().cerrarMesa(idMesa);
		respuesta.put("resultado", "OK");
	}
	catch (Exception e) {
		respuesta.put("resultado", "ERROR");
		respuesta.put("mensaje", e.getMessage());
	}
%>

<%= respuesta %>