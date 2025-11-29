package main.simulation;

import fileio.*;
import main.entity.air.Air;
import main.entity.air.types.*;
import main.entity.animal.Animal;
import main.entity.plant.Plant;
import main.entity.animal.types.*;
import main.entity.plant.types.*;
import main.entity.soil.Soil;
import main.entity.soil.types.*;
import main.entity.water.Water;

import java.util.List;

public class Territory {
    private int width;
    private int height;
    private Plant[][] plants;
    private Animal[][] animals;
    private Soil[][] soil;
    private Water[][] water;
    private Air[][] air;

    public Territory(TerritorySectionParamsInput territorySectionParams, int height, int width) {
        this.width = width;
        this.height = height;
        this.plants = new Plant[height][width];
        this.animals = new Animal[height][width];
        this.soil = new Soil[height][width];
        this.water = new Water[height][width];
        this.air = new Air[height][width];

        initializeSoil(this, territorySectionParams.getSoil());
        initializePlants(this, territorySectionParams.getPlants());
        initializeAnimals(this, territorySectionParams.getAnimals());
        initializeWater(this, territorySectionParams.getWater());
        initializeAir(this, territorySectionParams.getAir());
    }

    private void initializeSoil(Territory territory, List<SoilInput> soilInputs) {
        if (soilInputs == null) return;
        for (SoilInput soilInput : soilInputs) {
            Soil soil = switch (soilInput.getType()) {
                case "ForestSoil" -> new ForestSoil(soilInput.getName(), soilInput.getMass(), soilInput.getNitrogen(), soilInput.getWaterRetention(), soilInput.getSoilpH(), soilInput.getOrganicMatter(), soilInput.getLeafLitter());
                case "SwampSoil" -> new SwampSoil(soilInput.getName(), soilInput.getMass(), soilInput.getNitrogen(), soilInput.getWaterRetention(), soilInput.getSoilpH(), soilInput.getOrganicMatter(), soilInput.getWaterLogging());
                case "TundraSoil" -> new TundraSoil(soilInput.getName(), soilInput.getMass(), soilInput.getNitrogen(), soilInput.getWaterRetention(), soilInput.getSoilpH(), soilInput.getOrganicMatter(), soilInput.getPermafrostDepth());
                case "DesertSoil" -> new DesertSoil(soilInput.getName(), soilInput.getMass(), soilInput.getNitrogen(), soilInput.getWaterRetention(), soilInput.getSoilpH(), soilInput.getOrganicMatter(), soilInput.getSalinity());
                case "GrasslandSoil" -> new GrasslandSoil(soilInput.getName(), soilInput.getMass(), soilInput.getNitrogen(), soilInput.getWaterRetention(), soilInput.getSoilpH(), soilInput.getOrganicMatter(), soilInput.getLeafLitter());
                default -> null;
            };

            if (soil != null) {
                for (PairInput pairInput : soilInput.getSections()) {
                    territory.addSoil(soil, pairInput.getX(), pairInput.getY());
                }
            }
        }
    }

    private void initializePlants(Territory territory, List<PlantInput> plantInputs) {
        if (plantInputs == null) return;
        for (PlantInput plantInput : plantInputs) {
            Plant plant = switch (plantInput.getType()) {
                case "GymnospermsPlants" -> new GymnospermPlants(plantInput.getName(), plantInput.getMass());
                case "FloweringPlants" -> new FloweringPlant(plantInput.getName(), plantInput.getMass());
                case "Ferns" -> new Fern(plantInput.getName(), plantInput.getMass());
                case "Algae" -> new Algae(plantInput.getName(), plantInput.getMass());
                case "Mosses" -> new Moss(plantInput.getName(), plantInput.getMass());
                default -> null;
            };

            if (plant != null) {
                for (PairInput pairInput : plantInput.getSections()) {
                    territory.addPlant(plant, pairInput.getX(), pairInput.getY());
                }
            }
        }
    }

    private void initializeAnimals(Territory territory, List<AnimalInput> animalInputs) {
        if (animalInputs == null) return;
        for (AnimalInput animalInput : animalInputs) {
            Animal animal = switch (animalInput.getType()) {
                case "Parasites" -> new Parasite(animalInput.getName(), animalInput.getMass());
                case "Herbivores" -> new Herbivore(animalInput.getName(), animalInput.getMass());
                case "Carnivores" -> new Carnivore(animalInput.getName(), animalInput.getMass());
                case "Omnivores" -> new Omnivore(animalInput.getName(), animalInput.getMass());
                case "Detritivores" -> new Detritivore(animalInput.getName(), animalInput.getMass());
                default -> null;
            };

            if (animal != null) {
                for (PairInput pairInput : animalInput.getSections()) {
                    territory.addAnimal(animal, pairInput.getX(), pairInput.getY());
                    animal.setPosition(pairInput);
                }
            }
        }
    }

    private void initializeWater(Territory territory, List<WaterInput> waterInputs) {
        if (waterInputs == null) return;
        for (WaterInput waterInput : waterInputs) {
            Water water = new Water(waterInput.getName(), waterInput.getMass(), waterInput.getType(), waterInput.getPurity(), waterInput.getSalinity(), waterInput.getTurbidity(), waterInput.getContaminantIndex(), waterInput.getPH(), waterInput.isFrozen());

            for (PairInput pairInput : waterInput.getSections()) {
                territory.addWater(water, pairInput.getX(), pairInput.getY());
            }
        }
    }

    private void initializeAir(Territory territory, List<AirInput> airInputs) {
        if (airInputs == null) return;
        for (AirInput airInput : airInputs) {
            Air air = switch (airInput.getType()) {
                case "MountainAir" -> new MountainAir(airInput.getName(), airInput.getMass(), airInput.getHumidity(), airInput.getTemperature(), airInput.getOxygenLevel(), airInput.getAltitude());
                case "TemperateAir" -> new TemperateAir(airInput.getName(), airInput.getMass(), airInput.getHumidity(), airInput.getTemperature(), airInput.getOxygenLevel(), airInput.getPollenLevel());
                case "TropicalAir" -> new TropicalAir(airInput.getName(), airInput.getMass(), airInput.getHumidity(), airInput.getTemperature(), airInput.getOxygenLevel(), airInput.getCo2Level());
                case "PolarAir" -> new PolarAir(airInput.getName(), airInput.getMass(), airInput.getHumidity(), airInput.getTemperature(), airInput.getOxygenLevel(), airInput.getIceCrystalConcentration());
                case "DesertAir" -> new DesertAir(airInput.getName(), airInput.getMass(), airInput.getHumidity(), airInput.getTemperature(), airInput.getOxygenLevel(), airInput.getDustParticles());
                default -> null;
            };

            if (air != null) {
                for (PairInput pairInput : airInput.getSections()) {
                    territory.addAir(air, pairInput.getX(), pairInput.getY());
                }
            }
        }
    }

    public void addSoil(Soil soil, int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            this.soil[y][x] = soil;
        }
    }

    public void addWater(Water water, int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            this.water[y][x] = water;
        }
    }

    public void addAir(Air air, int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            this.air[y][x] = air;
        }
    }

    public void addPlant(Plant plant, int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            plants[y][x] = plant;
        }
    }

    public void addAnimal(Animal animal, int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            animals[y][x] = animal;
        }
    }

    public Plant getPlantAt(int x, int y) {
        return plants[y][x];
    }

    public Animal getAnimalAt(int x, int y) {
        return animals[y][x];
    }

    public Soil getSoilAt(int x, int y) {
        return soil[y][x];
    }

    public Water getWaterAt(int x, int y) {
        return water[y][x];
    }

    public Air getAirAt(int x, int y) {
        return air[y][x];
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }
}
