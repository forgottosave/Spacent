package control;

import onScreen.PopupMessage;
import screen.end.IntegrityFailEnd;
import entity.Ability;

import java.awt.*;
import java.util.Arrays;

public class ShipStatus {
    // general
    final GamePanel gamePanel;
    int worldsUnlocked;
    int secondsPlayed;
    // Ship
    int shipIntegrity;
    int livesOnBoard;
    int infectedOnBoard;
    // Systems
    int energy;
    int shields;
    int lifeSupportSystems;
    int oxygenLevel;
    int temperatureLevel;
    // Counters
    int healthDamageCounter;
    int meteorCounter;

    public ShipStatus(GamePanel gamePanel) {
        // general
        this.gamePanel = gamePanel;
        worldsUnlocked = 1; // TODO set to 0
        secondsPlayed = 0;
        // Ship
        shipIntegrity = 80;
        livesOnBoard = 0;
        infectedOnBoard = 0;
        // Energy
        energy = 0;
        shields = 0;
        lifeSupportSystems = 0;
        changeEnergy(25);
        changeLifeSupport(25);
        // Damage Counters
        healthDamageCounter = 0;
        setNewMeteorStrikeTime();
    }

    //// Ship concerning methods
    public void setNewMeteorStrikeTime() {
        meteorCounter = (int) ((Math.random() * 5 * 60 ) + 120);
    }

    //// System concerning methods
    public void changeEnergy(int value){
        energy = (energy + value) % 101;
    }
    public void changeLifeSupport(int value){
        lifeSupportSystems = (lifeSupportSystems + value) % 101;
        if (lifeSupportSystems > energy)
            lifeSupportSystems = energy;
        if (lifeSupportSystems < 0)
            lifeSupportSystems = 0;
        // change remaining energy for shields
        shields = (int) ((energy - lifeSupportSystems) * (lifeSupportSystems / 100.));
        if (shields > energy)
            shields = energy;
        // change oxygen and temperatureLevel
        oxygenLevel = (int) (5 + 15 * Math.sqrt(1.2 * lifeSupportSystems / 100.));
        temperatureLevel = 20 * lifeSupportSystems / 100;
    }

    /**
     * Updates ship status
     */
    public void update() {
        // update counters & ship info
        if (gamePanel.currentFrame() == 0) {
            secondsPlayed++;
            if (lifeSupportSystems < 70)
                healthDamageCounter++;
            meteorCounter--;
            livesOnBoard = Arrays.stream(gamePanel.getAllWorlds())
                    .mapToInt(w -> w.getEntities().size() - 1)
                    .sum();
            livesOnBoard = Arrays.stream(gamePanel.getAllWorlds())
                    .mapToInt(w -> w.getEntities().stream()
                            .filter(e -> e.getAbilities().contains(Ability.INFECTED))
                            .toList()
                            .size() - 1)
                    .sum();
        }
        // meteor strike
        if (meteorCounter <= 0) {
            setNewMeteorStrikeTime();
            shipIntegrity -= Math.max(0,Math.random() * 10. - shields / 10. - 1.);
            gamePanel.setPopup(new PopupMessage(gamePanel,
                    "Meteor Strike... Ship integrity at " + shipIntegrity + "%",
                    new Color(0xA92727),
                    3));
            System.out.println("Meteor Strike! Ship integrity at " + shipIntegrity + "%");
        }
        // health damage due to partially lacking life support
        if (healthDamageCounter > 60 * lifeSupportSystems * 1./3) {
            healthDamageCounter = 0;
            gamePanel.getPlayer().changeHealth(-1);
            System.out.println("Life Support Damaged... Lack of Oxygen");
        }
        // check for ship integrity
        if (shipIntegrity <= 0) {
            gamePanel.setEndScreen(new IntegrityFailEnd(gamePanel));
            gamePanel.setCurrentScreen(GamePanel.SCREEN_END);
        }
    }

    @Override
    public String toString() {
        return "ShipStatus{" +
                "roomsUnlocked_" + worldsUnlocked +
                "/shipIntegrity_" + shipIntegrity +
                "/energy_" + energy +
                "/shields_" + shields +
                "/lifeSupportSystems_" + lifeSupportSystems +
                "/healthDamageCounter_" + healthDamageCounter +
                "/meteorCounter_" + meteorCounter +
                "/secondsPlayed_" + secondsPlayed +
                '}';
    }
    public static ShipStatus toShipStatus(GamePanel gamePanel, String s) {
        // parse values
        String[] statusValues = s.substring(11,s.length() - 1).split("/");
        for (int i = 0; i < statusValues.length; i++) {
            statusValues[i] = statusValues[i].split("_")[1];
        }
        // create ShipStatus
        ShipStatus status = new ShipStatus(gamePanel);
        // set values
        status.worldsUnlocked = Integer.parseInt(statusValues[0]);
        status.shipIntegrity = Integer.parseInt(statusValues[1]);
        status.energy = Integer.parseInt(statusValues[2]);
        status.shields = Integer.parseInt(statusValues[3]);
        status.lifeSupportSystems = Integer.parseInt(statusValues[4]);
        status.healthDamageCounter = Integer.parseInt(statusValues[5]);
        status.meteorCounter = Integer.parseInt(statusValues[6]);
        status.secondsPlayed = Integer.parseInt(statusValues[7]);
        return status;
    }

    public void addAccessableWorld(int worldId) {
        if (worldId > worldsUnlocked)
            worldsUnlocked = worldId;
    }

    //// Fake Getter
    public String getTimePlayedString() {
        return (secondsPlayed / 60) + ":" + (secondsPlayed % 60);
    }
    //// Getter
    public int getWorldsUnlocked() {
        return worldsUnlocked;
    }
    public int getShields() {
        return shields;
    }
    public int getLifeSupportSystems() {
        return lifeSupportSystems;
    }
    public int getOxygenLevel() {
        return oxygenLevel;
    }
    public int getTemperatureLevel() {
        return temperatureLevel;
    }
}
