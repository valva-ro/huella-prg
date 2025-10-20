<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Entrada única: manda al controlador --%>
<%
  response.sendRedirect(request.getContextPath() + "/huella");
%>
