#!/bin/bash

echo "Testi: Autojen lisääminen."
echo "Odotettu: 2 onnistuu ja 1 epäonnistuu."
curl -H 'Content-Type: Application/Json' -d '{"registry": "AUTO-1", "brand": "Clay", "model": "X1", "year": 2010, "power": 30, "displacement": 1861}' localhost:8080/fleet/car/add
curl -H 'Content-Type: Application/Json' -d '{"registry": "AUTO-2", "brand": "Clay", "model": "X1", "year": 2010, "power": 30, "displacement": 1723}' localhost:8080/fleet/car/add
curl -H 'Content-Type: Application/Json' -d '{"registry": "AUTO-3", "brand": "Tesla", "model": "S1", "year": 2015, "power": 30, "displacement": 1950}' localhost:8080/fleet/car/add

echo "Testi: Muokataan auton AUTO-1 tietoja."
echo "Odotettu: "
curl -X PUT -H 'Content-Type: Application/Json' -d '{"registry": "AUTO-1", "brand": "Alcy"}' localhost:8080/fleet/car/modify
curl localhost:8080/fleet/car/search?registry=AUTO-1