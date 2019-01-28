package com.cschlay.car;

import com.cschlay.car.search.CarNotFoundException;
import com.cschlay.car.search.SearchEngine;
import org.junit.Test;
import java.util.Random;

public class ObjectTests {
    private final String[] BRANDS = {"Clay", "Alcy", "Yalc"};
    private final String[] MODELS = {"X1", "X2", "Y1"};
    private final String TESTIREKISTERI = "YKSIKKÖTESTI";
    private Random random = new Random();

    // Testataan automerkkien id hakua.
    @Test
    public void test1() {
        Brand brand = new Brand();

        for (String name : BRANDS) {
            brand.setName(name);
            assert(brand.getId() >= 0);
        }
    }

    // Testataan mallien id hakua.
    @Test
    public void test2() {
        Model model = new Model();

        for (String name : MODELS) {
            model.setName(name);
            assert(model.getId() >= 0);
        }
    }

    // Testaataan auton lisäystä, ja jolla testaan muutkin metodit.
    @Test
    public void test3() {
        Car car = new Car();
        car.setBrand(BRANDS[random.nextInt(BRANDS.length)]);
        car.setModel(MODELS[random.nextInt(MODELS.length)]);
        car.setRegistry(TESTIREKISTERI);
        car.setDisplacement(random.nextInt(1500) + 500);
        car.setPower(random.nextInt(10) + 5);
        car.setInspectionDate("2025-01-09");

        CarResponse response = car.add();
        assert(response.getSuccess());
    }

    // Testataan auton hakua.
    @Test
    public void test4() throws CarNotFoundException {
        Car testCar = (new SearchEngine()).searchCar(TESTIREKISTERI);
    }

    // Testaan muokkausta
    @Test
    public void test5() throws CarNotFoundException {
        Car testCar = (new SearchEngine()).searchCar(TESTIREKISTERI);
        testCar.setModel(MODELS[random.nextInt(MODELS.length)]);
        CarResponse response = testCar.modify();
        assert(response.getSuccess());
    }

    // Testaan testiauton poistoa.
    @Test
    public void test6() throws CarNotFoundException {
        Car testCar = (new SearchEngine()).searchCar(TESTIREKISTERI);
        CarResponse response = testCar.delete();
        assert(response.getSuccess());
    }
}
