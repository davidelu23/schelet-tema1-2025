# TerraBot - Environmental Exploration Simulation

A Java-based simulation system where an autonomous robot explores and interacts with diverse environmental territories, collecting data and improving ecological conditions.

## Architecture Overview

### Core Components

**SimulationManager**
Entry point that handles the parsing of the commands, simulation initialization and updates.

**Simulation**
Represents a single simulation instance containing:
- Territory grid with environmental entities
- Robot and its energy
- WeatherEvents updates
- Environment updates

**Robot**
Simple algorithm for movement, learning facts and enviorment improvement:
- Position tracking and pathfinding
- Energy-based actions (move, learn, improve, charge)
- Entity inventory (plants, animals, water)
- Knowledge base for learned facts

**Territory**
2D grid structure where each cell contains:
- Air layer (with weather events)
- Soil layer
- Optional: Plant, Animal, or Water entity

### Entity Hierarchy

```
Entity (abstract)
|-- Air
|   |-- DesertAir (sandstorms)
|   |-- MountainAir (hikers)
|   |-- PolarAir (storms)
|   |-- TemperateAir (seasons)
|   |-- TropicalAir (rainfall)
|-- Soil
|   |-- DesertSoil
|   |-- ForestSoil
|   |-- GrasslandSoil
|   |-- SwampSoil
|   |-- TundraSoil
|-- Plant
|   |-- Algae
|   |-- Moss
|   |-- Fern
|   |-- GymnospermPlants
|   |-- FloweringPlant
|-- Animal
|   |-- Carnivore
|   |-- Herbivore
|   |-- Omnivore
|   |-- Detritivore
|   |-- Parasite
|-- Water
```

## Interaction Flow

### 1. Simulation Lifecycle

```
START -> Initialize Territory -> Execute Commands -> Update Environment -> END
```

**Initialization**
- Create territory grid from configuration
- Spawn entities based on parameters
- Initialize robot at origin (0,0)

**Command Processing**
- Read command from input
- Update environment between commands (time-based)
- Execute command with validation
- Handle exceptions
- Output JSON response

### 2. Robot Movement

**Pathfinding Algorithm**
- Examines 4 adjacent cells (N, S, E, W)
- Calculates environmental quality score per cell
- Selects cell with lowest interaction probability/toxicity
- Energy cost equals the selected score
- Updates position if sufficient battery

**Energy Consumption**
- Movement: Variable (based on destination quality)
- Scan: 7 energy points
- Learn Fact: 2 energy points
- Improve Environment: 10 energy points

### 3. Entity Scanning

Robot identifies entities using sensory data:

| Entity | Color  | Sound | Smell |
|--------|--------|-------|-------|
| Animal | Yes    | Yes   | Yes   |
| Plant  | Yes    | No    | Yes   |
| Water  | Yes    | Yes   | No    |

**Scan Process**
1. Check sensory attributes
2. Match pattern to entity type
3. Add to robot's inventory
4. Record scan timestamp
5. Deduct energy cost

### 4. Knowledge System

**Learning Facts**
- Requires scanned entity in inventory
- Associates facts with entity names
- Stored as topic -> facts mapping

**Environmental Improvements**
- Requires both scanned entity AND learned facts
- Actions:
  - `plantVegetation`: Increases oxygen (+0.3)
  - `fertilizeSoil`: Adds organic matter (+0.3)
  - `increaseHumidity`: Raises air humidity (+0.2)
  - `increaseMoisture`: Improves soil water retention (+0.2)

### 5. Weather Events

Each air type manages timed events:

**Event Lifecycle**
- Activated via `changeWeatherConditions` command
- Duration counter set to 2 time units
- Effects apply to air properties
- Auto-reset when duration expires

### 6. Environment Updates

Between commands, entities autonomously update:

**Update Rules**
- **Water**: Updates every 2 time units (improves air, soil, plants, dries up)
- **Plants**: Updates every time unit (produces oxygen and dies)
- **Animals**: Updates every 2 time units (position changes, eats, dies)

## Exception Handling

All errors return standardized JSON with:
- Error type identifier
- Descriptive message
- Timestamp

**Exception Types**
- `SimulationAlreadyStartedException`: Multiple start attempts
- `SimulationNotStartedException`: Command before initialization
- `NotEnoughBatteryException`: Insufficient energy for action (also EnergyException for the limit case)
- `RobotChargingException`: Action attempted during charging
- `ObjectNotFoundException`: Scan pattern mismatch
- `SubjectNotYetSavedException`: Entity not in inventory
- `FactNotYetSavedException`: No facts learned for entity
- `WeatherChangeDoesNotAffectException`: Wrong air type for event

## Design Patterns

**Strategy Pattern**: Different `Entity` types define different behaviors via `updateEnvironment()`

**Template Method**: Base `Entity` class defines common structure

**Command Pattern**: Each user command encapsulated as method in `Commands` class

**Singleton-like Statics**: Weather events use static state shared across air instances

## USE OF AI:
I used ai to:
- generate different cases to test and fix errors(like for example finding which atributes should be normalized and when)
- change magic numbers with understandable variables
- Pretify the readme to look like a proper markdown file