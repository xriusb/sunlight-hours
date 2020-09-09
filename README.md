# Sunlight hours Calculator

###Set Up the Application
This is a SpringBoot Application 

###API End Points
- init​ method that takes a String containing a JSON describing the city, with this format:
  [{ neighborhood: <name_string>, apartments_height: <number>, buildings: [{name:
  <name_string>, apartments_count: <number>, distance: <number>}]}]
  
  POST /api/v1/sunlight-hours 
  
  Example Body: [
                    {
                      "neighborhood": "Poble_Nou",
                      "apartments_height": 4,
                      "buildings": [
                          {
                            "name": "Edificio_A",
                            "apartments_count": 3,
                            "distance": 2
                          },
                          {
                            "name": "Edificio_B",
                            "apartments_count": 2,
                            "distance": 2
                          }
                      ]
                    }
                  ]
  
 - getSunlightHours: method which takes a neighbourhood name, building name, and
   apartment number. It returns the sunlight hours as a string like “hh:mm:ss - hh:mm:ss” in 24hr format.
   GET /api/v1/sunlight-hours/{neighbourhood}/{building}/{apartment}
                  

