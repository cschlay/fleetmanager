package fi.eatech.fleetmanagerws.controller;

import com.cschlay.car.Car;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/fleet/car")
@RestController
public class CarController {

    /**
     * Lisää auton tietokantaan.
     * Jsonin pitää olla esimerkiksi seuraavanlainen (tiedot täytyy olla).
     *
     * {"brand": "Clay", "engine": 1, "model": "X1", "registry": "TESTI-AUTO", "year": 1980}
     *
     * @param car auton tiedot 'json'-muodossa.
     * @return ilmoitus siitä onnistuiko lisäys
     */
    @PostMapping("/add")
    public String addCar(@RequestBody Car car) {
        if (car.store())
            return "Auto lisätty tietokantaan.\n";
        else
            return "Autoa ei voitu lisätä.\n";
    }
}
