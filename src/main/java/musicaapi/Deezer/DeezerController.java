package musicaapi.Deezer;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeezerController {

    private final DeezerService deezerService;

    public DeezerController(DeezerService deezerService) {
        this.deezerService = deezerService;
    }

    @GetMapping("/deezer/buscar")
    public List<CancionExterna> buscar(@RequestParam String texto) {
        return deezerService.buscar(texto);
    }
}