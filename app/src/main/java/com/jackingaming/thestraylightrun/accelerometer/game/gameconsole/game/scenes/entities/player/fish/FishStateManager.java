package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;

import java.io.Serializable;

public class FishStateManager
        implements Serializable {
    public static final int BASE_BITE = 1, BASE_STRENGTH = 1, BASE_KICK = 0, BASE_STRIKE = 0, BASE_HORN = 0,
            BASE_DEFENSE = 0, BASE_AGILITY = 4, BASE_JUMP = 1;

    ////////////////////////////////////////////////////////////

    //ACTION STATES
    public enum ActionState {EAT, BITE, HURT, NONE;}

    //BODY
    //BODY TEXTURE and BODY SIZE determine the images of HEAD and BODY to use.
    public enum BodyTexture {
        SLICK(200, 0), SCALY(300, 2), SHELL(600, 4);

        BodyTexture(int cost, int defenseBonus) {
            this.cost = cost;
            this.defenseBonus = defenseBonus;
        }

        private int cost;
        private int defenseBonus;

        public int getCost() {
            return cost;
        }

        public int getDefenseBonus() {
            return defenseBonus;
        }
    }

    public enum BodySize {
        DECREASE(200, 0, 0, 0, 0), INCREASE(400, 10, 1, 1, -3);

        BodySize(int cost, int healthMaxBonus, int strengthBonus, int defenseBonus, int agilityBonus) {
            this.cost = cost;
            this.healthMaxBonus = healthMaxBonus;
            this.strengthBonus = strengthBonus;
            this.defenseBonus = defenseBonus;
            this.agilityBonus = agilityBonus;
        }

        private int cost;
        private int healthMaxBonus, strengthBonus, defenseBonus, agilityBonus;

        public int getCost() {
            return cost;
        }

        public int getHealthMaxBonus() {
            return healthMaxBonus;
        }

        public int getStrengthBonus() {
            return strengthBonus;
        }

        public int getDefenseBonus() {
            return defenseBonus;
        }

        public int getAgilityBonus() {
            return agilityBonus;
        }
    }

    public enum FinPectoral {
        ORIGINAL(200, 0, 0), COELAFISH(300, 1, 2), TACKLE(400, 2, 3);

        FinPectoral(int cost, int strengthBonus, int agilityBonus) {
            this.cost = cost;
            this.strengthBonus = strengthBonus;
            this.agilityBonus = agilityBonus;
        }

        private int cost;
        private int strengthBonus, agilityBonus;

        public int getCost() {
            return cost;
        }

        public int getStrengthBonus() {
            return strengthBonus;
        }

        public int getAgilityBonus() {
            return agilityBonus;
        }
    }

    public enum Tail {
        ORIGINAL(100, 0, 0, 0), COELAFISH(150, 3, 1, 1), TERATISU(200, 5, 2, 2),
        ZINICHTHY(300, 7, 3, 3), KURASELACHE(400, 15, 4, 4);

        Tail(int cost, int healthMaxBonus, int agilityBonus, int jumpBonus) {
            this.cost = cost;
            this.healthMaxBonus = healthMaxBonus;
            this.agilityBonus = agilityBonus;
            this.jumpBonus = jumpBonus;
        }

        private int cost;
        private int healthMaxBonus, agilityBonus, jumpBonus;

        public int getCost() {
            return cost;
        }

        public int getHealthMaxBonus() {
            return healthMaxBonus;
        }

        public int getAgilityBonus() {
            return agilityBonus;
        }

        public int getJumpBonus() {
            return jumpBonus;
        }
    }

    //HEAD
    public enum Jaws {
        ORIGINAL(10, 0), KURASELACHES(3000, 2), ZINICHTHY(300, 4)/*, SWORDFISH*/;

        Jaws(int cost, int damageBiteBonus) {
            this.cost = cost;
            this.damageBiteBonus = damageBiteBonus;
        }

        private int cost;
        private int damageBiteBonus;

        public int getCost() {
            return cost;
        }

        public int getDamageBiteBonus() {
            return damageBiteBonus;
        }
    }

    //HEAD-ATTACHMENT
    public enum Horn {ORIGINAL, SPIRALED, ANGLER, SWORDFISH, NONE;}

    //BODY-ATTACHMENT
    public enum FinDorsal {ORIGINAL, SAILING, KURASELACHE;}

    ////////////////////////////////////////////////////////////

    //INSTANCE FIELDS
    private transient Game game;
    private Player player;

    private ActionState currentActionState;

    private BodySize currentBodySize;
    private BodyTexture currentBodyTexture;

    private Jaws currentJaws;
    private Horn currentHorn;

    private FinPectoral currentFinPectoral;
    private Tail currentTail;
    private FinDorsal currentFinDorsal;

    private int damageBite, damageStrength, damageKick, damageStrike, damageHorn,
            defense, agility, jump;
    ////////////////////////////////////////////////////////////

    //CONSTRUCTOR with default values.
    public FishStateManager() {
        currentActionState = ActionState.NONE;

        currentBodySize = BodySize.DECREASE;
        currentBodyTexture = BodyTexture.SLICK;

        currentJaws = Jaws.ORIGINAL;
        currentHorn = Horn.NONE;

        currentFinPectoral = FinPectoral.ORIGINAL;
        currentTail = Tail.ORIGINAL;
        currentFinDorsal = FinDorsal.ORIGINAL;

        damageBite = BASE_BITE;
        damageStrength = BASE_STRENGTH;
        damageKick = BASE_KICK;
        damageStrike = BASE_STRIKE;
        damageHorn = BASE_HORN;
        defense = BASE_DEFENSE;
        agility = BASE_AGILITY;
        jump = BASE_JUMP;
    } // **** end FishStateManager(Handler) constructor ****

    public void init(Game game) {
        this.game = game;
        this.player = Player.getInstance();
    }

    ////////////////////////////////////////////////////////////
    //TODO: update BONUSES from the new body part.
    public void updatePlayerStats() {
        ((FishForm) player.getForm()).setHealthMax(20 + currentBodySize.getHealthMaxBonus() + currentTail.getHealthMaxBonus());
        ((FishForm) player.getForm()).setHealth(((FishForm) player.getForm()).getHealthMax());
        damageBite = BASE_BITE + currentJaws.getDamageBiteBonus();
        damageStrength = BASE_STRENGTH + currentBodySize.getStrengthBonus() + currentFinPectoral.getStrengthBonus();
        defense = BASE_DEFENSE + currentBodyTexture.getDefenseBonus() + currentBodySize.getDefenseBonus();
        agility = BASE_AGILITY + currentBodySize.getAgilityBonus() + currentFinPectoral.getAgilityBonus() + currentTail.getAgilityBonus();
        jump = BASE_JUMP + currentTail.getJumpBonus();
    }
    //TODO: USE THE new bonus values TO ACTUALLY CHANGE the damage-dealt-by-player, speed-of-player, defense-against-damage-taken-by-player, etc.

    // GETTERS AND SETTERS

    public void setGame(Game game) {
        this.game = game;
    }

    public ActionState getCurrentActionState() {
        return currentActionState;
    }

    public void setCurrentActionState(ActionState currentActionState) {
        this.currentActionState = currentActionState;
    }

    public Jaws getCurrentJaws() {
        return currentJaws;
    }

    public BodyTexture getCurrentBodyTexture() {
        return currentBodyTexture;
    }

    public BodySize getCurrentBodySize() {
        return currentBodySize;
    }

    public FinPectoral getCurrentFinPectoral() {
        return currentFinPectoral;
    }

    public Tail getCurrentTail() {
        return currentTail;
    }

    public FinDorsal getCurrentFinDorsal() {
        return currentFinDorsal;
    }

    public Horn getCurrentHorn() {
        return currentHorn;
    }

    public void setCurrentJaws(Jaws currentJaws) {
        this.currentJaws = currentJaws;
    }

    public void setCurrentBodyTexture(BodyTexture currentBodyTexture) {
        this.currentBodyTexture = currentBodyTexture;
    }

    public void setCurrentBodySize(BodySize currentBodySize) {
        this.currentBodySize = currentBodySize;
    }

    public void setCurrentFinPectoral(FinPectoral currentFinPectoral) {
        this.currentFinPectoral = currentFinPectoral;
    }

    public void setCurrentTail(Tail currentTail) {
        this.currentTail = currentTail;
    }

    public void setCurrentFinDorsal(FinDorsal currentFinDorsal) {
        this.currentFinDorsal = currentFinDorsal;
    }

    public void setCurrentHorn(Horn currentHorn) {
        this.currentHorn = currentHorn;
    }

    public int getDamageBite() {
        return damageBite;
    }

    public int getDamageStrength() {
        return damageStrength;
    }

    public int getDamageKick() {
        return damageKick;
    }

    public int getDamageStrike() {
        return damageStrike;
    }

    public int getDamageHorn() {
        return damageHorn;
    }

    public int getDefense() {
        return defense;
    }

    public int getAgility() {
        return agility;
    }

    public int getJump() {
        return jump;
    }
}