<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.*, edu.uclm.esi.disoft.comandas.dominio.Manager" %>

<%
	JSONArray categorias=Manager.get().getCategorias();
%>

<%= categorias %>