<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.*, edu.uclm.esi.disoft.comandas.dominio.Manager" %>

<%
	response.addHeader("Access-Control-Allow-Origin", "*");
	String p=request.getParameter("p");
	JSONObject jso=new JSONObject(p);
	int idMesa=jso.getInt("id");
	//int id = Integer.parseInt(idMesa);
	JSONObject mesa=Manager.get().getEstadoMesa(idMesa);
%>

<%= mesa %>