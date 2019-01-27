# fleetmanager
Fleet Manager Spring Boot

## Asennus (Linux)
1. Asenna PostgreSQL.
2. Luo tietokanta nimellä *cschlay* ja käyttäjä samalla nimellä.
3. Aseta salasanaksi *koodikas*.
4. Suorita annettu skripti `database_intial_linux.sh`.
Tämä luo tarvittavat taulut ja alustaa testidatalla.

## Tietokannasta
Moottorin teho ilmaistaan yksikössä **kW** ja koko yksikössä **cm^3**.

## Testaaminen
REST -apin testaamisessa on käytetty cURL:ia.

Esimerkkejä testeistä, tieto on peräisin [tästä artikkelista](https://www.baeldung.com/curl-rest).
```
# Testataan autolistausta
curl -H 'Content-Type: application/json' -d '{"minYear:1900", "maxYear":2019, "brand":"Clay", "model":"X1"}' localhost:8080/fleet/car/listing
```