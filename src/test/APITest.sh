#!/bin/bash

echo "== AUTOJEN LISÄÄMINEN  =="

echo "Lisätään {"registry": "AUTO-1", "brand": "Clay", "model": "X1", "year": 2010, "power": 30, "displacement": 1861, "inspectionDate": "2020-01-20"}."
curl -H 'Content-Type: Application/Json' -d '{"registry": "AUTO-1", "brand": "Clay", "model": "X1", "year": 2010, "power": 30, "displacement": 1861, "inspectionDate": "2020-01-20"}' localhost:8080/fleet/car/add
echo ""

echo "Lisätään {"registry": "AUTO-2", "brand": "Clay", "model": "X1", "year": 2010, "power": 30, "displacement": 1723, "inspectionDate": "2021-05-23"}."
curl -H 'Content-Type: Application/Json' -d '{"registry": "AUTO-2", "brand": "Clay", "model": "X1", "year": 2010, "power": 30, "displacement": 1723, "inspectionDate": "2021-05-23"}' localhost:8080/fleet/car/add
echo ""

echo "Seuraavaksi pitäisi tulla ilmoitus, että lisäys ei onnistu."
curl -H 'Content-Type: Application/Json' -d '{"registry": "AUTO-3", "brand": "Tesla", "model": "S1", "year": 2015, "power": 30, "displacement": 1950}' localhost:8080/fleet/car/add
echo ""

echo "== AUTOLISTAUS =="
echo "Haetaan listaus autoista rajauksella: {"minYear": 1900, "maxYear": 2019, "brand": "Clay", "model": "X1"}."
echo "Tuloksena pitäisi olla lista, jossa on useita autoja ihmiselle vaikeasti luettavana listana."
echo ""
curl -H 'Content-Type: Application/Json' -d '{"minYear": 1900, "maxYear": 2019, "brand": "Clay", "model": "X1"}' localhost:8080/fleet/car/listing
echo ""
echo ""
echo "Haetaan listaus rajauksella {"brand": "Yalc"}"
curl -H 'Content-Type: Application/Json' -d '{"brand": "Yalc"}' localhost:8080/fleet/car/listing
echo ""
echo ""

echo "== AUTON TIETOJEN HAKU =="
echo "Haetaan auton AUTO-1 tietoja"
curl localhost:8080/fleet/car/search?registry=AUTO-1
echo ""
echo ""
echo "Seuraavaa autoa ei löydy."
curl localhost:8080/fleet/car/search?registry=EI-OLE
echo ""
echo ""

echo "== AUTON MUOKKAUS == "
echo "Muokataan auton AUTO-1 tietoja siten, että vaihdetaan auton merkki."
curl -X PUT -H 'Content-Type: Application/Json' -d '{"registry": "AUTO-1", "brand": "Alcy"}' localhost:8080/fleet/car/modify
echo ""
echo "Tarkistetaan onko auton tieto muuttunut."
curl localhost:8080/fleet/car/search?registry=AUTO-1
echo ""
echo "Vaihdetaan nyt vuosimalli"
curl -X PUT -H 'Content-Type: Application/Json' -d '{"registry": "AUTO-1", "year": "1997"}' localhost:8080/fleet/car/modify
echo ""
echo "Tarkistetaan onko auton tieto muuttunut."
curl localhost:8080/fleet/car/search?registry=AUTO-1
echo ""
echo ""
echo "Seuraavaa autoa ei voi muokata."
curl -X PUT -H 'Content-Type: Application/Json' -d '{"registry": "AUTO-EI", "brand": "Alcy"}' localhost:8080/fleet/car/modify
echo ""
echo ""

echo "== TESTIAUTOJEN POISTAMINEN =="
curl -X DELETE localhost:8080/fleet/car/delete?registry=AUTO-1
curl -X DELETE localhost:8080/fleet/car/delete?registry=AUTO-2
echo "Seuraava poiston ei pitäisi onnistua."
curl -X DELETE localhost:8080/fleet/car/delete?registry=AUTO-3
echo ""
