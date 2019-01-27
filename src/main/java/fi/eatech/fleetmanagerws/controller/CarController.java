package fi.eatech.fleetmanagerws.controller;

import com.cschlay.car.Car;
import com.cschlay.car.CarResponse;
import com.cschlay.car.search.CarNotFoundException;
import com.cschlay.car.search.SearchEngine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
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
     * Muokkaa auton tietoja.
     * Kaikkia paitsi katsastuspäivämäärää.
     *
     * Ainoastaan vaadittu argumentti on rekisterinumero ja sen lisäksi muutettava tieto.
     *
     * Esimerkiksi: {"registry": "TESTI-QLW-951", "year": 2019}
     *
     * @param car auton tiedot annetaan json-muodossa
     * @return ilmoitus siitä onnistuiko muokkaus
     */
    @PutMapping("/modify")
    public ResponseEntity<String> modifyCar(@RequestBody Car car) {
        try {
            Car modifiableCar = (new SearchEngine()).searchCar(car.getRegistry());

            // Muutetaan auton tietoja.
            String b = car.getBrandName();
            if (b != null)
                modifiableCar.setBrand(b);

            String m = car.getModelName();
            if (m != null)
                modifiableCar.setModel(m);

            int d = car.getDisplacement();
            if (d != 0)
                modifiableCar.setDisplacement(d);

            int p = car.getPower();
            if (p != 0)
                modifiableCar.setPower(p);

            int y = car.getYear();
            if (y != 0)
                modifiableCar.setYear(y);

            CarResponse response = modifiableCar.modify();

            if (response.getSuccess())
                return response.getResponse(HttpStatus.CREATED);
            else
                return response.getResponse(HttpStatus.BAD_REQUEST);
        }
        catch (CarNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Autoa ei löydetty tietokannasta.",HttpStatus.BAD_REQUEST);
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
        try {
            return (new SearchEngine().searchCar(registry));
        }
        catch (CarNotFoundException e) {
            return null;
        }
    }
}
