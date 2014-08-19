package me.faris.rotmk.realms;

import java.util.ArrayList;
import java.util.List;

import me.faris.rotmk.helpers.exceptions.DuplicateRealmException;

import org.bukkit.WorldCreator;

public class Realm extends RealmBase {
	public static final List<Realm> REALM_LIST = new ArrayList<Realm>();
	public static final Realm REALM_NEXUS = addRealm(new Realm(0, "Nexus"));
	public static final Realm REALM_VAULT = addRealm(new Realm(1, "Vault"));
	public static final Realm REALM_BEHOLDER = addRealm(new Realm(2, "Beholder"));
	public static final Realm REALM_CYCLOPS = addRealm(new Realm(3, "Cyclops"));
	public static final Realm REALM_DJINN = addRealm(new Realm(4, "Djinn"));
	public static final Realm REALM_MEDUSA = addRealm(new Realm(5, "Medusa"));
	public static final Realm REALM_OGRE = addRealm(new Realm(6, "Ogre"));

	private int worldID = -1;

	public Realm(int realmID, String realmName) {
		super(realmID, realmName);
		if (realmID != 0) this.worldID = this.realmID;
		else this.worldID = -1;
	}

	public void initRealm() {
		String worldName = this.isNexus() ? "Nexus" : "Realm" + this.worldID;
		this.realmWorld = this.getServer().getWorld(worldName);
		if (this.realmWorld == null) {
			this.realmWorld = this.getServer().createWorld(WorldCreator.name(worldName));
		}
	}

	public int getWorldID() {
		return this.worldID;
	}

	public void setWorldID(int worldID) {
		this.worldID = worldID;
	}

	/**private boolean worldExists() {
		File worldFile = new File(this.getServer().getWorldContainer(), this.realmName);
		return worldFile.exists() && worldFile.isDirectory();
	}**/

	private static Realm addRealm(Realm realm) {
		try {
			for (Realm existingRealm : REALM_LIST) {
				if (existingRealm.getID() == realm.getID()) throw new DuplicateRealmException("The Realm with the ID " + realm.getID() + " already exists!", realm.getID());
			}
			if (!REALM_LIST.contains(realm)) REALM_LIST.add(realm);
			else throw new DuplicateRealmException("The Realm '" + realm.getName() + "' has already been registered!", realm.getID());
			return realm;
		} catch (DuplicateRealmException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
