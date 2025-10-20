<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Huella de Carbono (PRG · MVC)</title>
  <style>
    body{font-family:system-ui,Arial,sans-serif;margin:2rem}
    fieldset{max-width:560px;padding:1rem 1.2rem}
    label{display:inline-block;width:160px;margin:.3rem 0}
    input,select,button{padding:.35rem .5rem}
    .error{color:#b00020;margin-top:.6rem}
    .res{margin-top:1rem;font-size:1.05rem}
  </style>
</head>
<body>
<h1>Huella de Carbono (PRG · MVC)</h1>

<form method="post" action="${pageContext.request.contextPath}/huella">
  <fieldset>
    <legend>Datos</legend>

    <label for="transporte">Transporte</label>
    <select id="transporte" name="transporte">
      <option value="CAR">Coche</option>
      <option value="BUS">Autobús</option>
      <option value="TRAIN">Tren</option>
      <option value="BIKE">Bicicleta</option>
      <option value="FOOT">A pie</option>
    </select><br/>

    <label for="km">Km diarios</label>
    <input id="km" name="km" type="text" placeholder="Ej: 12.5"/><br/>

    <label for="dias">Días/semana</label>
    <input id="dias" name="dias" type="number" min="1" max="7" value="5"/><br/>

    <label for="op">Operación</label>
    <select id="op" name="op">
      <option value="CALC_SEMANAL">Calcular semanal</option>
      <option value="CLASIFICAR_IMPACTO">Clasificar impacto</option>
      <option value="PROPONER_COMPENSACION">Proponer compensación</option>
    </select><br/>

    <button type="submit">Calcular</button>

    <c:if test="${not empty error}">
      <div class="error"><c:out value="${error}"/></div>
    </c:if>
  </fieldset>
</form>

<c:if test="${not empty op}">
  <div class="res">
    <c:choose>
      <c:when test="${op == 'CALC_SEMANAL'}">
        Esta semana emitirás
        <strong><fmt:formatNumber value="${kg}" maxFractionDigits="2"/> kg CO₂</strong>.
      </c:when>
      <c:when test="${op == 'CLASIFICAR_IMPACTO'}">
        Impacto <strong><c:out value="${impacto}"/></strong>
        (<fmt:formatNumber value="${kg}" maxFractionDigits="2"/> kg CO₂).
      </c:when>
      <c:when test="${op == 'PROPONER_COMPENSACION'}">
        Para <fmt:formatNumber value="${kg}" maxFractionDigits="2"/> kg CO₂:
        <strong><c:out value="${comp}"/></strong>.
      </c:when>
    </c:choose>
  </div>
</c:if>

</body>
</html>
