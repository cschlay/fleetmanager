package fi.eatech.fleetmanagerws.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/fleet")
@RestController
public class FleetController {

    @GetMapping("/health")
    public ResponseEntity getHealth() {
        return ResponseEntity.ok("System up");
    }
}
