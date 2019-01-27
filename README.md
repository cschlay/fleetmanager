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
6. Kokeile esimerkiksi hakemiston *src/test/APITest.sh* testi skriptiä
   ```
   fleetmanager/test/$ source APITest.sh
   ``` 
7. Hakemistossa *docs* löytyy tarkemmat ohjeet API:n kutsuille.

## Tietokannasta
Moottorin teho ilmaistaan yksikössä kiloWatti (**kW**) ja koko yksikössä **cm^3**.
Heikolla autotietoudella oletin, että moottorin koko on englanniksi "displacement".