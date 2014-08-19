package me.faris.rotmk;

import me.faris.rotmk.helpers.Cuboid;
import me.faris.rotmk.realms.Realm;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    // MySQL
    private String sqlHost = "", sqlPort = "", sqlDatabase = "", sqlUsername = "", sqlPassword = "";

    // Portals
    private List<Cuboid> portalList = new ArrayList<Cuboid>(6);

    private int classCost = 250;
    private double expShareRadius = 36D;

    public int getClassCost() {
        return this.classCost;
    }

    public double getExpShareRadius() {
        return this.expShareRadius;
    }

    public String getMySQL(int position) {
        if (position == 1) return this.sqlHost;
        else if (position == 2) return this.sqlPort;
        else if (position == 3) return this.sqlDatabase;
        else if (position == 4) return this.sqlUsername;
        else if (position == 5) return this.sqlPassword;
        else return "";
    }

    public Cuboid getPortal(Realm realm) {
        if (realm == Realm.REALM_VAULT) return this.portalList.get(0);
        else if (realm == Realm.REALM_BEHOLDER) return this.portalList.get(1);
        else if (realm == Realm.REALM_CYCLOPS) return this.portalList.get(2);
        else if (realm == Realm.REALM_DJINN) return this.portalList.get(3);
        else if (realm == Realm.REALM_MEDUSA) return this.portalList.get(4);
        else if (realm == Realm.REALM_OGRE) return this.portalList.get(5);
        else return null;
    }

    public List<Cuboid> getPortals() {
        return this.portalList;
    }

    public Realm getRealm(int realmIndex) {
        if (realmIndex == 0) return Realm.REALM_VAULT;
        else if (realmIndex == 1) return Realm.REALM_BEHOLDER;
        else if (realmIndex == 2) return Realm.REALM_CYCLOPS;
        else if (realmIndex == 3) return Realm.REALM_DJINN;
        else if (realmIndex == 4) return Realm.REALM_MEDUSA;
        else if (realmIndex == 5) return Realm.REALM_OGRE;
        return Realm.REALM_NEXUS;
    }

    public void setClassCost(int classCost) {
        this.classCost = classCost;
    }

    public void setExpShareRadius(double shareRadius) {
        this.expShareRadius = shareRadius;
    }

    public void setMySQL(int position, String value) {
        if (position == 1) this.sqlHost = value;
        else if (position == 2) this.sqlPort = value;
        else if (position == 3) this.sqlDatabase = value;
        else if (position == 4) this.sqlUsername = value;
        else if (position == 5) this.sqlPassword = value;
    }

    public void setPortal(Realm realm, Cuboid cuboid) {
        if (realm == Realm.REALM_VAULT) this.setPortalList(0, cuboid);
        else if (realm == Realm.REALM_BEHOLDER) this.setPortalList(1, cuboid);
        else if (realm == Realm.REALM_CYCLOPS) this.setPortalList(2, cuboid);
        else if (realm == Realm.REALM_DJINN) this.setPortalList(3, cuboid);
        else if (realm == Realm.REALM_MEDUSA) this.setPortalList(4, cuboid);
        else if (realm == Realm.REALM_OGRE) this.setPortalList(5, cuboid);
    }

    private void setPortalList(int index, Cuboid cuboid) {
        if (index < 0) index *= -1;
        if (this.portalList.size() > index) {
            this.portalList.set(index, cuboid);
        } else {
            this.portalList.add(index, cuboid);
        }
    }

}
