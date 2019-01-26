package com.cschlay.car;

import org.junit.Test;

public class ObjectTests {
    private final String[] testBrands = {"Clay", "Alcy", "Yalc", "Tesla"};

    @Test
    public void brand() {
        Brand brand = new Brand();

        for (String name : testBrands) {
            brand.setName(name);
            System.out.println(brand.getId());
        }

        System.out.println("Kaikkien paitsi viimeisen pitää olla suurempia kuin 0.");
    }

    @Test
    public void car_insertion() {
        Car car = new Car();
        car.setModel("X1");
        car.setBrand("Clay");
        car.setEngine(2);
        car.setRegistry("TESTI-OK");
        car.setYear(2010);

        car.store();
    }
}
