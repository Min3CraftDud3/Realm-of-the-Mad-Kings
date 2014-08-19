package me.faris.rotmk.helpers.exceptions;

public class DuplicateRealmException extends Exception {
    private static final long serialVersionUID = 1L;
    private int realmID = 0;

    public DuplicateRealmException(String message, int realmID) {
        super(message);
        this.realmID = realmID;
    }

    public int getID() {
        return this.realmID;
    }
}
