package me.faris.rotmk.helpers;

import org.bukkit.ChatColor;

public class RotMKClass {
    private String className = "";
    private ClassStatistic classAttack, classDexterity, classDefense, classHealth, classMagic, classSpeed;
    private ChatColor classColour = ChatColor.BLACK;
    private ClassStatistic incrementHealth, incrementMagic, incrementAttack, incrementDefense, incrementDexterity, incrementSpeed;

    public RotMKClass(String className, ChatColor classColour, ClassStatistic maxHealth, ClassStatistic maxMagic, ClassStatistic maxAttack, ClassStatistic maxDefense, ClassStatistic maxDexterity, ClassStatistic maxSpeed) {
        this.className = className;
        this.classColour = classColour;
        this.classHealth = maxHealth;
        this.classMagic = maxMagic;
        this.classAttack = maxAttack;
        this.classDefense = maxDefense;
        this.classDexterity = maxDexterity;
        this.classSpeed = maxSpeed;
    }

    public ChatColor getColour() {
        return this.classColour;
    }

    public ClassStatistic getAttack() {
        return this.classAttack;
    }

    public int getIncrementAttack() {
        return this.incrementAttack.getRandom();
    }

    public ClassStatistic getDefense() {
        return this.classDefense;
    }

    public int getIncrementDefense() {
        return this.incrementDefense.getRandom();
    }

    public ClassStatistic getDexterity() {
        return this.classDexterity;
    }

    public int getIncrementDexterity() {
        return this.incrementDexterity.getRandom();
    }

    public ClassStatistic getHealth() {
        return this.classHealth;
    }

    public int getIncrementHealth() {
        return this.incrementHealth.getRandom();
    }

    public ClassStatistic getMagic() {
        return this.classMagic;
    }

    public int getIncrementMagic() {
        return this.incrementMagic.getRandom();
    }

    public ClassStatistic getSpeed() {
        return this.classSpeed;
    }

    public int getIncrementSpeed() {
        return this.incrementSpeed.getRandom();
    }

    public String getName() {
        return this.className;
    }

    public RotMKClass setIncrementAttack(ClassStatistic incrementAttack) {
        this.incrementAttack = incrementAttack;
        return this;
    }

    public RotMKClass setIncrementDefense(ClassStatistic incrementDefense) {
        this.incrementDefense = incrementDefense;
        return this;
    }

    public RotMKClass setIncrementDexterity(ClassStatistic incrementDexterity) {
        this.incrementDexterity = incrementDexterity;
        return this;
    }

    public RotMKClass setIncrementHealth(ClassStatistic incrementHealth) {
        this.incrementHealth = incrementHealth;
        return this;
    }

    public RotMKClass setIncrementMagic(ClassStatistic incrementMagic) {
        this.incrementMagic = incrementMagic;
        return this;
    }

    public RotMKClass setIncrementSpeed(ClassStatistic incrementSpeed) {
        this.incrementSpeed = incrementSpeed;
        return this;
    }

    @Override
    public String toString() {
        return this.className + ":" + this.classAttack + ":" + this.classDefense + ":" + this.classDexterity + ":" + this.classHealth + ":" + this.classMagic + ":" + this.classSpeed;
    }
}
