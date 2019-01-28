package com.cschlay.car;

import org.junit.Test;

public class ObjectTests {
    private final String[] BRANDS = {"Clay", "Alcy", "Yalc"};


    @Test
    public void brand() {
        Brand brand = new Brand();

        for (String name : BRANDS) {
            brand.setName(name);
            assert(brand.getId() >= 0);
        }
    }

    @Test
    public void car_insertion() {
        Car car = new Car();
        car.setModel("X1");
        car.setBrand("Clay");
        car.setRegistry("TESTI-OK");
        car.setYear(2010);

        CarResponse response = car.add();
        assert(response.getSuccess());

        car.delete();
    }
}
