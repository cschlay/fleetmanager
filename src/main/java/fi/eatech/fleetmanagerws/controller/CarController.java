package fi.eatech.fleetmanagerws.controller;

import com.cschlay.car.Car;
import com.cschlay.car.CarResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @param car auton tiedot annetaan json-muodossa
     * @return ilmoitus siitä onnistuiko lisäys
     */
    @PostMapping("/add")
    public ResponseEntity<String > addCar(@RequestBody Car car) {
        String message;

        if (car.store()) {
            message = String.format("Auto %s lisättiin tietokantaan.\n", car.getRegistry());
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        else {
            message = String.format("Autoa %s ei voitu lisätä.\n", car.getRegistry());
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Muokkaa auton tietoja annetun jsonin mukaan.
     * Ainoastaan vaadittu argumentti on rekisterinumero ja sen lisäksi muutettava tieto.
     *
     * Esimerkiksi: {"registry": "TESTI-QLW-951", "year": 2019}
     *
     * @param car auton tiedot annetaan json-muodossa
     * @return ilmoitus siitä onnistuiko muokkaus
     */
    @PutMapping("/modify")
    public ResponseEntity<String> modifyCar(@RequestBody Car car) {
        String message;

        if (car.modify()) {
            message = String.format("Muokattiin auton %s tietoja.\n", car.getRegistry());
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        else {
            message = String.format("Auton %s tietoja ei voitu muokata.\n", car.getRegistry());
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Poistaa auton rekisterinumeron perusteella.
     *
     * @param registry auton rekisterinumero
     * @return ilmoitus, onnistuiko auton poistaminen
     */
    @DeleteMapping("/delete/{registry}")
    public ResponseEntity<String> deleteCar(@RequestParam String registry) {
        Car car = new Car(registry);
        CarResponse response = car.delete();

        if (response.getSuccess())
            return response.getResponse(HttpStatus.CREATED);
        else
            return response.getResponse(HttpStatus.BAD_REQUEST);
    }
}
