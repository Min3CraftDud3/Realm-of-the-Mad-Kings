package lib.simplecache.cache;

import lib.simplecache.CachedTable;
import lib.simplecache.events.TableRecieveEvent;
import lib.simplecache.threads.ThreadedQuery;
import lib.simplecache.threads.ThreadedUpdate;
import lib.simplecache.util.ResultParser;
import lib.simplecache.util.Statements;
import me.faris.rotmk.Main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Goblom
 */
public class CachedPlayerStats extends CachedTable {
    private List<Row> rows = new ArrayList<Row>();

    public CachedPlayerStats() {
        super("Player_Stats");
    }

    @Override
    public void createTable() {
        String sql = Statements.createTable(tableName, "id int(11) NOT NULL AUTO_INCREMENT", "uuid varchar(255) NOT NULL", "class varchar(255)", "kills int", "deaths int", "PRIMARY KEY (`id`)") + "AUTO_INCREMENT=1";

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
                                String clazz = rp.getString("class");
                                int kills = rp.getInt("kills");
                                int deaths = rp.getInt("deaths");

                                rows.add(new Row(id, uuid, clazz, kills, deaths));
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

    public static final String DEFAULT_CLASS = "None";

    public void newEntry(UUID uuid) {
        int id = 0;
        for (Row row : rows) {
            if (row.getID() > id) {
                id = row.getID();
            }
        }

        rows.add(new Row((id + 1), uuid, DEFAULT_CLASS, 0, 0));
        String sql = Statements.insertInto(tableName, new String[]{"id", "uuid", "class", "kills", "deaths"}, new String[]{"" + (id + 1), "" + uuid, DEFAULT_CLASS, "0", "0"});

        new ThreadedUpdate(Main.getInstance().getMySQLConnection(), sql).start();
    }

    public class Row extends CachedTable.CachedRow {

        private final UUID uuid;
        private String className;
        private int kills, deaths;

        public Row(int id, UUID uuid, String clazz, int kills, int deaths) {
            super(id);

            this.uuid = uuid;
            this.className = clazz;
            this.kills = kills;
            this.deaths = deaths;
        }

        public UUID getUUID() {
            return uuid;
        }

        public String getClassName() {
            return className;
        }

        public int getKills() {
            return kills;
        }

        public int getDeaths() {
            return deaths;
        }

        public void incrementDeaths(int increment) {
            if (Integer.MAX_VALUE - this.deaths >= increment) this.deaths += increment;
            else this.deaths = Integer.MAX_VALUE;
            sendUpdate("deaths", Integer.toString(this.deaths));
        }

        public void incrementKills(int increment) {
            if (Integer.MAX_VALUE - this.kills >= increment) this.kills += increment;
            else this.kills = Integer.MAX_VALUE;
            sendUpdate("kills", Integer.toString(this.kills));
        }

        public void setClass(String clazz) {
            this.className = clazz;
            sendUpdate("class", clazz);
        }

        public void setKills(int kills) {
            this.kills = kills;
            sendUpdate("kills", Integer.toString(kills));
        }

        public void setDeaths(int deaths) {
            this.deaths = deaths;
            sendUpdate("deaths", Integer.toString(deaths));
        }

        protected void sendUpdate(String column, String value) {
            String sql = Statements.update(tableName, column, value) + Statements.where("`uuid`='" + uuid + "'");

            new ThreadedUpdate(Main.getInstance().getMySQLConnection(), sql).start();
        }
    }
}
