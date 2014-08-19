package me.faris.rotmk.helpers;

public enum PotionID {
    WATER(0), REGENERATION(8193), SWIFTNESS(8194), FIRE_RESISTANCE(8195), HEALING(8197), STRENGTH(8201);

    private short potionID = 0;

    private PotionID(int potionID) {
        this.potionID = potionID > Short.MAX_VALUE ? Short.MAX_VALUE : (potionID < 0 ? (short) 0 : (short) potionID);
    }

    public short getPotionID() {
        return (short) this.potionID;
    }
}
