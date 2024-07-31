package de.dasshorty.collectyourteam.backend.image;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController()
@RequestMapping("/v1/image/")
public class ImageController {

    private static final Logger log = LoggerFactory.getLogger(ImageController.class);
    private final ImageRepository imageRepository;

    @Autowired
    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("image") MultipartFile file) {

        log.info("Uploading image: {}", file.getOriginalFilename());

        ImageDto imageDto = new ImageDto();
        try {
            imageDto.setImage(new Binary(file.getBytes()));

            ImageDto save = this.imageRepository.save(imageDto);

            return ResponseEntity.ok(save.getId().toHexString());

        } catch (Exception e) {
            e.fillInStackTrace();
            return ResponseEntity.internalServerError().body(e);
        }

    }

    @GetMapping("/view/{id}")
    public ResponseEntity<?> view(@PathVariable("id") ObjectId id) {

        return this.imageRepository.findById(id)
                .map(imageDto -> ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageDto.getImage().getData()))
                .orElse(ResponseEntity.notFound().build());

    }
}
