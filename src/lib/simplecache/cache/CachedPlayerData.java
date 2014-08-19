package lib.simplecache.cache;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lib.simplecache.CachedTable;
import lib.simplecache.events.TableRecieveEvent;
import lib.simplecache.threads.ThreadedQuery;
import lib.simplecache.threads.ThreadedUpdate;
import lib.simplecache.util.ResultParser;
import lib.simplecache.util.Statements;
import me.faris.rotmk.Main;
import me.faris.rotmk.helpers.RotMKClass;

/**
 * @author Goblom
 */
public class CachedPlayerData extends CachedTable {

	private List<Row> rows = new ArrayList<Row>();

	public CachedPlayerData() {
		super("Player_Data");
	}

	@Override
	public void createTable() {
		String sql = Statements.createTable(tableName, "id int(11) NOT NULL AUTO_INCREMENT", "uuid varchar(255) NOT NULL", "coins int", "attack int", "defense int", "dexterity int", "speed int", "magic int", "PRIMARY KEY (`id`)") + "AUTO_INCREMENT=1";

		new ThreadedUpdate(Main.getInstance().getMySQLConnection(), sql).start();
	}

	@Override
	public void update() {
		this.rows = null;
		this.rows = new ArrayList<Row>();

		String sql = "SELECT * FROM `" + tableName + "`";

		new ThreadedQuery(Main.getInstance().getMySQLConnection(), sql, new ThreadedQuery.DataHandler() {

			@Override
			public void onDataRecieve(TableRecieveEvent event) {
				if (event.getResult() != null) {
					try {
						ResultParser rp = new ResultParser(event.getResult(), false);

						if (rp != null) {
							rp.get().beforeFirst();
							while (rp.get().next()) {
								int id = rp.getInt("id");
								UUID uuid = UUID.fromString(rp.getString("uuid"));
								int coins = rp.getInt("coins");
								int attack = rp.getInt("attack");
								int defense = rp.getInt("defense");
								int dexterity = rp.getInt("dexterity");
								int speed = rp.getInt("speed");
								int magic = rp.getInt("magic");

								rows.add(new Row(id, uuid, coins, attack, defense, dexterity, speed, magic));
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Row getRow(int id) {
		for (Row row : rows) {
			if (row.getID() == id) {
				return row;
			}
		}
		return null;
	}

	public Row getRow(UUID uuid) {
		Row userRow = null;

		for (Row row : rows) {
			if (row.getUUID() == uuid) {
				userRow = row;
			}
		}
		return userRow;
	}

	public void newEntry(UUID uuid) {
		int id = 0;
		for (Row row : rows) {
			if (row.getID() > id) {
				id = row.getID();
			}
		}

		rows.add(new Row((id + 1), uuid, 0, 0, 0, 0, 0, 0));
		String sql = Statements.insertInto(tableName, new String[] { "id", "uuid", "coins", "attack", "defense", "dexterity", "speed", "magic" }, new String[] { "" + (id + 1), "" + uuid, "0", "0", "0", "0", "0", "0" });

		new ThreadedUpdate(Main.getInstance().getMySQLConnection(), sql).start();
	}

	public class Row extends CachedTable.CachedRow {

		private final UUID uuid;
		private int coins, attack, defense, dexterity, speed, magic;

		public Row(int id, UUID uuid, int coins, int attack, int defense, int dexterity, int speed, int magic) {
			super(id);
			this.uuid = uuid;

			this.coins = coins;
			this.attack = attack;
			this.defense = defense;
			this.dexterity = dexterity;
			this.speed = speed;
			this.magic = magic;
		}

		public UUID getUUID() {
			return uuid;
		}

		public int getCoins() {
			return coins;
		}

		public int getAttack() {
			return attack;
		}

		public int getDefense() {
			return defense;
		}

		public int getDexterity() {
			return dexterity;
		}

		public int getSpeed() {
			return speed;
		}

		public int getMagic() {
			return magic;
		}

		public void incrementCoins(int coins) {
			if (Integer.MAX_VALUE - coins >= this.coins) this.coins += coins;
			else this.coins = Integer.MAX_VALUE;
			if (this.coins < 0) this.coins = 0;
			sendUpdate("coins", this.coins);
		}

		public void incrementAttack(int attack) {
			RotMKClass playerClass = Main.getInstance().getClass(this.uuid);
			int maxValue = playerClass != null ? playerClass.getAttack().getMaximum() : 10;
			if (this.attack + attack > maxValue) this.attack = maxValue;
			else this.attack += attack;
			int minimumAttack = playerClass != null ? playerClass.getAttack().getMinimum() : 2;
			if (this.attack < minimumAttack) this.attack = minimumAttack;
			sendUpdate("attack", this.attack);
		}

		public void incrementDefense(int defense) {
			RotMKClass playerClass = Main.getInstance().getClass(this.uuid);
			int maxValue = playerClass != null ? playerClass.getDefense().getMaximum() : 10;
			if (this.defense + defense > maxValue) this.defense = maxValue;
			else this.defense += defense;
			int minimumDefense = playerClass != null ? playerClass.getDefense().getMinimum() : 2;
			if (this.defense < minimumDefense) this.defense = minimumDefense;
			sendUpdate("defense", this.defense);
		}

		public void incrementDexterity(int dexterity) {
			RotMKClass playerClass = Main.getInstance().getClass(this.uuid);
			int maxValue = playerClass != null ? playerClass.getDexterity().getMaximum() : 10;
			if (this.dexterity + dexterity > maxValue) this.dexterity = maxValue;
			else this.dexterity += dexterity;
			int minimumDexterity = playerClass != null ? playerClass.getDexterity().getMinimum() : 2;
			if (this.dexterity < minimumDexterity) this.dexterity = minimumDexterity;
			sendUpdate("dexterity", this.dexterity);
		}

		public void incrementSpeed(int speed) {
			RotMKClass playerClass = Main.getInstance().getClass(this.uuid);
			int maxValue = playerClass != null ? playerClass.getSpeed().getMaximum() : 10;
			if (this.speed + speed > maxValue) this.speed = maxValue;
			else this.speed += speed;
			int minimumSpeed = playerClass != null ? playerClass.getSpeed().getMinimum() : 2;
			if (this.speed < minimumSpeed) this.speed = minimumSpeed;
			sendUpdate("speed", this.speed);
		}

		public void incrementMagic(int magic) {
			RotMKClass playerClass = Main.getInstance().getClass(this.uuid);
			int maxValue = playerClass != null ? playerClass.getMagic().getMaximum() : 100;
			if (this.magic + magic > maxValue) this.magic = maxValue;
			else this.magic += magic;
			int minimumMagic = playerClass != null ? playerClass.getMagic().getMinimum() : 50;
			if (this.magic < minimumMagic) this.magic = minimumMagic;
			sendUpdate("magic", this.magic);
		}

		public void setCoins(int coins) {
			this.coins = coins;
			sendUpdate("coins", coins);
		}

		public void setAttack(int attack) {
			this.attack = attack;
			sendUpdate("attack", attack);
		}

		public void setDefense(int defense) {
			this.defense = defense;
			sendUpdate("defense", defense);
		}

		public void setDexterity(int dexterity) {
			this.dexterity = dexterity;
			sendUpdate("dexterity", dexterity);
		}

		public void setSpeed(int speed) {
			this.speed = speed;
			sendUpdate("speed", speed);
		}

		public void setMagic(int magic) {
			this.magic = magic;
			sendUpdate("magic", magic);
		}

		protected void sendUpdate(String column, int value) {
			String sql = Statements.update(tableName, column, Integer.toString(value)) + Statements.where("`uuid`='" + uuid + "'");

			new ThreadedUpdate(Main.getInstance().getMySQLConnection(), sql).start();
		}
	}
}
