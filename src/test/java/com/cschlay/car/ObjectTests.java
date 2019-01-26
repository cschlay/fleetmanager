package com.cschlay.car;

import org.junit.Test;

public class ObjectTests {
    private final String[] testBrands = {"Clay", "Alcy", "Yalc", "Tesla"};

    @Test
    public void Brand() {
        for (String name : testBrands) {
            Brand brand = new Brand(name);
            System.out.println(brand.getId());
        }

        System.out.println("Kaikkien paitsi viimeisen pitää olla suurempia kuin 0.");
    }
}
