package org.lms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class BaseController {

    @GetMapping
    public ResponseEntity<String> get() {
        String robotArt = """
              ╔═══════╗
              ║ ^   ^ ║
              ║   ▼   ║
              ║ ───── ║
              ╚═╦═══╦═╝
                ║   ║
              ══╩═══╩══
            HELLO SPHERE!
            """;
        return ResponseEntity.ok(robotArt);
    }
}
