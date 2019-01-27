# Rajapinnan kutsut

## Auton lisääminen
Auton tiedot annetaan json -muodossa.
POST -metodin yleinen muoto on siis
```
curl -H 'Content-Type: Application/Json' -d '{"registry": "<rekisterinro>", "brand": "<merkki>", "model": "<malli>", "year": <vuosi>, "power": <moottorin teho kW>, "displacement": <moottorin koko cm^3>}' localhost:8080/fleet/car/add
```
Osa tiedoista yhdistetään jo tietokannassa olevaan, kuten auton merkki ja malli.
Niiden täytyy siis olla jo tietokannassa ennen kuin niitä käytetään.

**Esimerkki**
```
curl -H 'Content-Type: Application/Json' -d '{"registry": "TESTI-AUTO", "brand": "Clay", "model": "X1", "year": 2019, "power": 30, "displacement": 1800}' localhost:8080/fleet/car/add
```

## Auton tietojen muokkaaminen
Muokattavan auton tiedot annetaan json -muodossa.
Ainoastaan rekisterinumero on pakollinen ja jokin muokattava ominaisuus.

**Esimerkki**
```
curl -X PUT -H 'Content-Type: Application/Json' -d '{"registry": "TESTI-AUTO", "vuosimalli": 1980}' localhost:8080/fleet/car/modify
```