package fi.eatech.fleetmanagerws.controller;

import com.cschlay.car.Car;
import com.cschlay.car.CarResponse;
import com.cschlay.car.search.SearchEngine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/fleet/car")
@RestController
public class CarController {

    /**
     * Lisää auton tietokantaan.
     *
     * Esimerkki sallitusta json -argumentista.
     * {"registry": "TESTI-AUTO", "brand": "Clay", "model": "X1", "year": 2019, "power": 30, "displacement": 1800}'
     *
     * @param car auton tiedot annetaan json-muodossa
     * @return ilmoitus siitä onnistuiko lisäys
     */
    @PostMapping("/add")
    public ResponseEntity<String > addCar(@RequestBody Car car) {
        CarResponse response = car.add();

        if (response.getSuccess())
            return response.getResponse(HttpStatus.CREATED);
        else
            return response.getResponse(HttpStatus.BAD_REQUEST);
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
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCar(@RequestParam String registry) {
        Car car = new Car();
        car.setRegistry(registry);
        CarResponse response = car.delete();

        if (response.getSuccess())
            return response.getResponse(HttpStatus.CREATED);
        else
            return response.getResponse(HttpStatus.BAD_REQUEST);
    }

    /**
     * Listaa tietokannassa sisältäviä autoja.
     * Oletetaan, että kaikki rajaustoiminnon tiedot ovat käytössä.
     *
     * @param search rajaustoiminnot
     * @return lista autoista
     */
    @PostMapping("/listing")
    public @ResponseBody List<Car> carListing(@RequestBody SearchEngine search) {
        return search.carListing();
    }

    /**
     * Hakee yksittäisen auton tietoja rekisterinumeron perusteella.
     *
     * @param registry auton rekisterinumero
     * @return auton tiedot json -objektina.
     */
    @GetMapping("/search")
    public @ResponseBody Car findCar(@RequestParam String registry) {
        return new Car(registry);
    }


}
