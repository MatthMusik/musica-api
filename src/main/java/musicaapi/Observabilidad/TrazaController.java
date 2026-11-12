package musicaapi.observabilidad;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.tracing.Tracer;

@RestController
public class TrazaController {

    private final Tracer tracer;
    private static final Logger logger = LoggerFactory.getLogger(TrazaController.class);

    public TrazaController(Tracer tracer) {
        this.tracer = tracer;
    }

    @GetMapping("/traza")
    public Map<String, String> consultarTraza() {
        logger.info("Se consultó la traza de la solicitud");
        Map<String, String> respuesta = new LinkedHashMap<>();

        if (tracer.currentSpan() != null) {
            respuesta.put("traceId", tracer.currentSpan().context().traceId());
            respuesta.put("spanId", tracer.currentSpan().context().spanId());
        }

        respuesta.put("mensaje", "Solicitud procesada con trazabilidad");
        return respuesta;
    }
}