package com.ain.controller;


import com.ain.model.CarbonFootprintService;
import com.ain.model.OperationType;
import com.ain.model.TransportType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@WebServlet("/huella")
public class CarbonFootprintServlet extends HttpServlet {

    private final CarbonFootprintService service = new CarbonFootprintService();

    // Definimos el comportamiento en un mapa, para evitar if/switchs
    private final Map<OperationType, Function<Double, String>> operationHandlers = Map.of(
            OperationType.CALC_SEMANAL, kg -> String.format("?op=CALC_SEMANAL&kg=%.2f", kg),
            OperationType.CLASIFICAR_IMPACTO, kg -> {
                String impact = service.classifyImpact(kg);
                return String.format("?op=CLASIFICAR_IMPACTO&kg=%.2f&impact=%s",
                        kg, encode(impact));
            },
            OperationType.PROPONER_COMPENSACION, kg -> {
                String comp = service.proposeCompensation(kg);
                return String.format("?op=PROPONER_COMPENSACION&kg=%.2f&comp=%s",
                        kg, encode(comp));
            }
    );

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("UTF-8");

        String transportStr = request.getParameter("transporte");
        String kmStr = request.getParameter("km");
        String daysStr = request.getParameter("dias");
        String opStr = request.getParameter("op");

        // Validation with functional Optional pipeline
        TransportType transport = TransportType.fromString(transportStr);
        OperationType op = OperationType.fromString(opStr);

        String error = Optional.ofNullable(validateInputs(transport, kmStr, daysStr, op))
                .filter(s -> !s.isEmpty())
                .orElse(null);

        if (error != null) {
            redirectWithError(response, request, error);
            return;
        }

        double km = Double.parseDouble(kmStr);
        int days = Integer.parseInt(daysStr);
        double kg = service.calculateWeekly(transport, km, days);

        // Sabemos que el mapa tiene definidas las operaciones que permitimos con cada logica, asi que
        // solo obtenemos la que queremos del map y ejecutamos
        // -> Si hubiera mas logica (y no solo manejo de strings), se podria hasta plantear un patron strategy
        String redirectUrl = Optional.ofNullable(operationHandlers.get(op))
                .map(handler -> request.getContextPath() + "/huella" + handler.apply(kg))
                .orElseGet(() -> request.getContextPath() + "/huella?error=" + encode("Unknown operation"));

        response.sendRedirect(redirectUrl);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<String> params = List.of("op", "error", "kg", "impact", "comp");

        params.forEach(p -> {
            String value = request.getParameter(p);
            if (value != null && !value.isEmpty()) {
                if ("kg".equals(p)) {
                    try {
                        request.setAttribute("kg", Double.parseDouble(value));
                    } catch (NumberFormatException e) {
                        request.setAttribute("error", "Invalid kg value");
                    }
                } else {
                    request.setAttribute(p, value);
                }
            }
        });

        request.getRequestDispatcher("/WEB-INF/views/huella.jsp")
                .forward(request, response);
    }

    private String validateInputs(TransportType transport, String kmStr, String daysStr, OperationType op) {
        if (transport == null) return "Invalid transport selected.";
        if (op == null) return "Invalid operation.";
        if (kmStr == null || kmStr.isEmpty()) return "Please enter your daily kilometers.";
        if (daysStr == null || daysStr.isEmpty()) return "Please enter how many days per week.";

        try {
            double km = Double.parseDouble(kmStr);
            int days = Integer.parseInt(daysStr);
            if (km <= 0) return "Daily kilometers must be greater than zero.";
            if (days < 1 || days > 7) return "Days must be between 1 and 7.";
        } catch (NumberFormatException e) {
            return "Invalid numeric format.";
        }

        return null;
    }

    private void redirectWithError(HttpServletResponse response, HttpServletRequest request, String error)
            throws IOException {
        String msg = encode(error);
        response.sendRedirect(request.getContextPath() + "/huella?error=" + msg);
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
