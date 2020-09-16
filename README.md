# Sunlight hours Calculator

### Run the application
- This is a SpringBoot application.
- After downloading the application, from the root directory execute the command **mvn clean package**
- To start the applications, execute the command **java -jar target/sunlight-hours-0.0.1.jar**
- This will start a Tomcat Server on the 8080 port

### Objectives
- The aim of it is to display the sunlight hours that a given apartment receives in a day, with this constraints: 
    - Neighbourhoods have the buildings distributed by the provided distances
    - In those neighbourhoods, the buildings are always aligned east to west
    - The sun rises in the east and travels at a constant radial speed until setting
    - The only shadows created in a neighbourhood are artefacts of the buildings in it
    - We consider an apartment receives sunlight when either its eastern or western exterior wall is fully covered in 
    sunlight and/or when the sun is directly overhead
    - There is only one apartment per floor; in a building with N floors they are numbered from 0 to N-1

### How the application works
- The setting of the sunrise and sunset time, and the earth radius is done through application properties.
- All building has a width and height of one unit.
- The distance between buildings could be provided in decimals.
- Once the application receives the city, it creates the neighborhood with the buildings in a 2 dimensional map, setting 
the center in the middle of the distance created for all the buildings and the distance between them.
- Once the application receives an apartment sunlight hours, it set the position of the sun on the distance specified 
by the radius of the earth, and make the calculation

### API End Points
- **init**​ method that takes a String containing a JSON describing the city, with this format:
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
  
 - **getSunlightHours** method which takes a neighbourhood name, building name, and
   apartment number. It returns the sunlight hours as a string like “hh:mm:ss - hh:mm:ss” in 24hr format.
   
   GET /api/v1/sunlight-hours/{neighbourhood}/{building}/{apartment}
                  

