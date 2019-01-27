# fleetmanager
Fleet Manager -toteutukseni Etteplan MORE:n backend -ennakkotehtävänä.

## Asennus (Linux/Ubuntu 18.10)
1. Asenna PostgreSQL [ohjeet täällä](https://www.postgresql.org/download/).
2. Luo tietokanta ja käyttäjä nimellä *cschlay* ja aseta salasanaksi *koodikas*.
   ```
   cl@lab$ sudo -i -u postgres
   postgres@lab$ createuser --interactive
   postgres@lab$ createdb cschlay
   postgres@lab$ psql postgres
   postgres=> ALTER USER cschlay WITH PASSWORD 'koodikas';
   postgres=> \q
   # CTRL + D uloskirjaukseen.
   ```
3. Kloonaa projekti.
   ```
   $ git clone https://github.com/cschlay/fleetmanager.git
   ```
4. Suorita asennuskripti hakemistossa *fleetmanager*.
   Tämä luo tarvittavat taulut ja alustaa testidatalla.

   ```
   fleetmanager/$ source database_intial_linux.sh
   ```
5. Suorita ohjelma hakemistossa fleetmanager ja aja komento tai vaihtoehtoisesti jollakin IDE:llä.
   ```
   fleetmanager/$ mvn spring-boot:run
   ```
6. Kokeile esimerkiksi hakemiston *src/test/APITest.sh* testiskriptiä
   ```
   fleetmanager/test/$ source APITest.sh
   ``` 
   
## Tietokannasta
Moottorin teho ilmaistaan yksikössä kiloWatti (**kW**) ja koko yksikössä **cm^3**.
Heikolla autotietoudella oletin, että moottorin koko on englanniksi "displacement".

## Rajapinnan kutsut

### Auton lisääminen
Auton tiedot annetaan json -muodossa.
POST -metodin yleinen muoto on siis
```
curl -H 'Content-Type: Application/Json' -d '{"registry": "<rekisterinro>", "brand": "<merkki>", "model": "<malli>", "year": <vuosi>, "power": <moottorin teho kW>, "displacement": <moottorin koko cm^3>, "inspectionDate": "<YYYY-MM-DD>"}' localhost:8080/fleet/car/add
```
Osa tiedoista yhdistetään jo tietokannassa olevaan, kuten auton merkki ja malli.
Niiden täytyy siis olla jo tietokannassa ennen kuin niitä käytetään.

**Esimerkki**
```
curl -H 'Content-Type: Application/Json' -d '{"registry": "AUTO-2", "brand": "Clay", "model": "X1", "year": 2010, "power": 30, "displacement": 1723, "inspectionDate": "2021-05-23"}' localhost:8080/fleet/car/add
```

### Auton tietojen muokkaaminen
Muokattavan auton tiedot annetaan json -muodossa.
Ainoastaan rekisterinumero on pakollinen ja sen lisäksi jokin muokattava ominaisuus.

**Esimerkki**
```
curl -X PUT -H 'Content-Type: Application/Json' -d '{"registry": "TESTI-AUTO", "vuosimalli": 1980}' localhost:8080/fleet/car/modify
```

### Auton poistaminen
Auto poistetaan rekisterinumeron perusteella eli osoitteen loppuosaan rekisterinumero.
Pitäisi myös automaattisesti poistaa moottorin tiedot ja katsauspäivämäärän, ne ovat eri taulussa.

**Esimerkki**
```
curl -X DELETE localhost:8080/fleet/car/delete?registry=TESTI-AUTO
```

### Tietojen hakeminen
Haetaan auton rekisterinumeron perusteella vastaava kuin auton poistamisessa.

**Esimerkki**
```
curl localhost:8080/fleet/car/search?registry=TESTI-AUTO
```

### Autolistaus
Autolistauksen haku toimii siten, että argumenttina annetaan json, joka sisältää jonkin rajoituksista.
Yksi rajoite riittää, vuosiluvuissa käytetään tarpeeksi suuria oletusarvoja.
```
curl -H 'Content-Type: Application/Json' -d '{"minYear": 1900, "maxYear": 2019, "model": "X1"}' localhost:8080/fleet/car/listing
```
