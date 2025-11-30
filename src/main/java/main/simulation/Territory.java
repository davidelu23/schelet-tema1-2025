package main.simulation;

import fileio.AirInput;
import fileio.AnimalInput;
import fileio.PairInput;
import fileio.PlantInput;
import fileio.SoilInput;
import fileio.TerritorySectionParamsInput;
import fileio.WaterInput;
import main.entity.air.Air;
import main.entity.air.types.DesertAir;
import main.entity.air.types.MountainAir;
import main.entity.air.types.PolarAir;
import main.entity.air.types.TemperateAir;
import main.entity.air.types.TropicalAir;
import main.entity.animal.Animal;
import main.entity.animal.types.Carnivore;
import main.entity.animal.types.Detritivore;
import main.entity.animal.types.Herbivore;
import main.entity.animal.types.Omnivore;
import main.entity.animal.types.Parasite;
import main.entity.plant.Plant;
import main.entity.plant.types.Algae;
import main.entity.plant.types.Fern;
import main.entity.plant.types.FloweringPlant;
import main.entity.plant.types.GymnospermPlants;
import main.entity.plant.types.Moss;
import main.entity.soil.Soil;
import main.entity.soil.types.DesertSoil;
import main.entity.soil.types.ForestSoil;
import main.entity.soil.types.GrasslandSoil;
import main.entity.soil.types.SwampSoil;
import main.entity.soil.types.TundraSoil;
import main.entity.water.Water;

import java.util.List;

public final class Territory {
    private int width;
    private int height;
    private Plant[][] plants;
    private Animal[][] animals;
    private Soil[][] soil;
    private Water[][] water;
    private Air[][] air;

    public Territory(final TerritorySectionParamsInput territorySectionParams,
                     final int height, final int width) {
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

    private void initializeSoil(final Territory territory, final List<SoilInput> soilInputs) {
        if (soilInputs == null) {
            return;
        }
        for (SoilInput soilInput : soilInputs) {
            for (PairInput pairInput : soilInput.getSections()) {
                Soil newSoil = switch (soilInput.getType()) {
                    case "ForestSoil" -> new ForestSoil(soilInput.getName(), soilInput.getMass(),
                            soilInput.getNitrogen(), soilInput.getWaterRetention(),
                            soilInput.getSoilpH(), soilInput.getOrganicMatter(),
                            soilInput.getLeafLitter());
                    case "SwampSoil" -> new SwampSoil(soilInput.getName(), soilInput.getMass(),
                            soilInput.getNitrogen(), soilInput.getWaterRetention(),
                            soilInput.getSoilpH(), soilInput.getOrganicMatter(),
                            soilInput.getWaterLogging());
                    case "TundraSoil" -> new TundraSoil(soilInput.getName(), soilInput.getMass(),
                            soilInput.getNitrogen(), soilInput.getWaterRetention(),
                            soilInput.getSoilpH(), soilInput.getOrganicMatter(),
                            soilInput.getPermafrostDepth());
                    case "DesertSoil" -> new DesertSoil(soilInput.getName(), soilInput.getMass(),
                            soilInput.getNitrogen(), soilInput.getWaterRetention(),
                            soilInput.getSoilpH(), soilInput.getOrganicMatter(),
                            soilInput.getSalinity());
                    case "GrasslandSoil" ->
                            new GrasslandSoil(soilInput.getName(), soilInput.getMass(),
                                    soilInput.getNitrogen(), soilInput.getWaterRetention(),
                                    soilInput.getSoilpH(), soilInput.getOrganicMatter(),
                                    soilInput.getRootDensity());
                    default -> null;
                };

                if (newSoil != null) {
                    territory.addSoil(newSoil, pairInput.getX(), pairInput.getY());
                }
            }
        }
    }

    private void initializePlants(final Territory territory, final List<PlantInput> plantInputs) {
        if (plantInputs == null) {
            return;
        }
        for (PlantInput plantInput : plantInputs) {
            for (PairInput pairInput : plantInput.getSections()) {
                Plant plant = switch (plantInput.getType()) {
                    case "GymnospermsPlants" ->
                            new GymnospermPlants(plantInput.getName(), plantInput.getMass());
                    case "FloweringPlants" ->
                            new FloweringPlant(plantInput.getName(), plantInput.getMass());
                    case "Ferns" -> new Fern(plantInput.getName(), plantInput.getMass());
                    case "Algae" -> new Algae(plantInput.getName(), plantInput.getMass());
                    case "Mosses" -> new Moss(plantInput.getName(), plantInput.getMass());
                    default -> null;
                };

                if (plant != null) {
                    territory.addPlant(plant, pairInput.getX(), pairInput.getY());
                }
            }
        }
    }

    private void initializeAnimals(final Territory territory,
                                   final List<AnimalInput> animalInputs) {
        if (animalInputs == null) {
            return;
        }
        for (AnimalInput animalInput : animalInputs) {
            for (PairInput pairInput : animalInput.getSections()) {
                Animal animal = switch (animalInput.getType()) {
                    case "Parasites" ->
                            new Parasite(animalInput.getName(), animalInput.getMass());
                    case "Herbivores" ->
                            new Herbivore(animalInput.getName(), animalInput.getMass());
                    case "Carnivores" ->
                            new Carnivore(animalInput.getName(), animalInput.getMass());
                    case "Omnivores" ->
                            new Omnivore(animalInput.getName(), animalInput.getMass());
                    case "Detritivores" ->
                            new Detritivore(animalInput.getName(), animalInput.getMass());
                    default -> null;
                };

                if (animal != null) {
                    territory.addAnimal(animal, pairInput.getX(), pairInput.getY());
                }
            }
        }
    }

    private void initializeWater(final Territory territory, final List<WaterInput> waterInputs) {
        if (waterInputs == null) {
            return;
        }
        for (WaterInput waterInput : waterInputs) {
            for (PairInput pairInput : waterInput.getSections()) {
                Water newWater = new Water(waterInput.getName(), waterInput.getMass(),
                        waterInput.getType(), waterInput.getSalinity(), waterInput.getPH(),
                        waterInput.getPurity(), waterInput.getTurbidity(),
                        waterInput.getContaminantIndex(), waterInput.isFrozen());
                territory.addWater(newWater, pairInput.getX(), pairInput.getY());
            }
        }
    }

    private void initializeAir(final Territory territory, final List<AirInput> airInputs) {
        if (airInputs == null) {
            return;
        }
        for (AirInput airInput : airInputs) {
            for (PairInput pairInput : airInput.getSections()) {
                Air newAir = switch (airInput.getType()) {
                    case "MountainAir" -> new MountainAir(airInput.getName(), airInput.getMass(),
                            airInput.getHumidity(), airInput.getTemperature(),
                            airInput.getOxygenLevel(), airInput.getAltitude());
                    case "TemperateAir" ->
                            new TemperateAir(airInput.getName(), airInput.getMass(),
                                    airInput.getHumidity(), airInput.getTemperature(),
                                    airInput.getOxygenLevel(), airInput.getPollenLevel());
                    case "TropicalAir" -> new TropicalAir(airInput.getName(), airInput.getMass(),
                            airInput.getHumidity(), airInput.getTemperature(),
                            airInput.getOxygenLevel(), airInput.getCo2Level());
                    case "PolarAir" -> new PolarAir(airInput.getName(), airInput.getMass(),
                            airInput.getHumidity(), airInput.getTemperature(),
                            airInput.getOxygenLevel(),
                            airInput.getIceCrystalConcentration());
                    case "DesertAir" -> new DesertAir(airInput.getName(), airInput.getMass(),
                            airInput.getHumidity(), airInput.getTemperature(),
                            airInput.getOxygenLevel(), airInput.getDustParticles());
                    default -> null;
                };

                if (newAir != null) {
                    territory.addAir(newAir, pairInput.getX(), pairInput.getY());
                }
            }
        }
    }

    /**
     * Adds soil to the territory.
     * @param newSoil The soil to add.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void addSoil(final Soil newSoil, final int x, final int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            this.soil[y][x] = newSoil;
        }
    }

    /**
     * Adds water to the territory.
     * @param newWater The water to add.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void addWater(final Water newWater, final int x, final int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            this.water[y][x] = newWater;
        }
    }

    /**
     * Adds air to the territory.
     * @param newAir The air to add.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void addAir(final Air newAir, final int x, final int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            this.air[y][x] = newAir;
        }
    }

    /**
     * Adds a plant to the territory.
     * @param plant The plant to add.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void addPlant(final Plant plant, final int x, final int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            plants[y][x] = plant;
        }
    }

    /**
     * Removes a plant from the territory.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void removePlant(final int x, final int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            plants[y][x] = null;
        }
    }

    /**
     * Removes water from the territory.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void removeWater(final int x, final int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            water[y][x] = null;
        }
    }

    /**
     * Adds an animal to the territory.
     * @param animal The animal to add.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void addAnimal(final Animal animal, final int x, final int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            animals[y][x] = animal;
        }
    }

    /**
     * Removes an animal from the territory.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void removeAnimal(final int x, final int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            animals[y][x] = null;
        }
    }

    /**
     * Gets the plant at the given coordinates.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The plant at the given coordinates.
     */
    public Plant getPlantAt(final int x, final int y) {
        return plants[y][x];
    }

    /**
     * Gets the animal at the given coordinates.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The animal at the given coordinates.
     */
    public Animal getAnimalAt(final int x, final int y) {
        return animals[y][x];
    }

    /**
     * Gets the soil at the given coordinates.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The soil at the given coordinates.
     */
    public Soil getSoilAt(final int x, final int y) {
        return soil[y][x];
    }

    /**
     * Gets the water at the given coordinates.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The water at the given coordinates.
     */
    public Water getWaterAt(final int x, final int y) {
        return water[y][x];
    }

    /**
     * Gets the air at the given coordinates.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The air at the given coordinates.
     */
    public Air getAirAt(final int x, final int y) {
        return air[y][x];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
