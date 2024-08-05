package de.dasshorty.collectyourteam.backend.server;

import de.dasshorty.collectyourteam.backend.image.ImageDto;
import de.dasshorty.collectyourteam.backend.image.ImageRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/server")
public class ServerController {

    private final ServerRepository serverRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public ServerController(ServerRepository serverRepository, ImageRepository imageRepository) {
        this.serverRepository = serverRepository;
        this.imageRepository = imageRepository;
    }

    @GetMapping
    public ResponseEntity<?> getServer() {

        ServerDto first = this.serverRepository.findAll().getFirst();

        ServerBody body = new ServerBody(
                first.getId().toHexString(),
                first.getServerName(),
                first.getServerDescription(),
                first.getLogo().getId().toHexString(),
                first.getBanner().getId().toHexString()
        );

        return ResponseEntity.ok(body);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createServer(ServerDto dto) {

        if (dto == null) {
            return ResponseEntity.badRequest().body("ServerDto is null");
        }

        if (this.serverRepository.count() > 0) {
            return ResponseEntity.badRequest().body("Server already exists");
        }

        this.serverRepository.save(dto);

        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/logo")
    public ResponseEntity<?> updateLogo(@RequestParam ObjectId image) {

        Optional<ImageDto> optional = this.imageRepository.findById(image);

        if (optional.isEmpty()) {
            return ResponseEntity.badRequest().body("Image not found");
        }

        ServerDto first = this.serverRepository.findAll().getFirst();
        first.setLogo(optional.get());

        return ResponseEntity.ok(this.serverRepository.save(first));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/banner")
    public ResponseEntity<?> updateBanner(@RequestParam ObjectId image) {

        Optional<ImageDto> optional = this.imageRepository.findById(image);

        if (optional.isEmpty()) {
            return ResponseEntity.badRequest().body("Image not found");
        }

        ServerDto first = this.serverRepository.findAll().getFirst();
        first.setBanner(optional.get());

        return ResponseEntity.ok(this.serverRepository.save(first));
    }

}
