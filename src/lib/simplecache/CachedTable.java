package lib.simplecache;

/*
 * Copyright 2014 Goblom.
 */

/**
 * @author Goblom
 */
public abstract class CachedTable {

    protected final String tableName;

    public CachedTable(String tableName) {
        this.tableName = tableName;
    }

    /*
     * Create Necessary columns and values for this table
     */
    public abstract void createTable();

    /*
     * Used to Gather all data from the table and cycle through and cach each row.
     */
    public abstract void update();

    /*
     * Cycle through each Row and get the row with the given id
     */
    public abstract <T extends CachedRow> T getRow(int id);

    public abstract class CachedRow {
        final int id;

        public CachedRow(int id) {
            this.id = id;
        }

        /*
         * The rows ID. This should come from a colum named ID that is AUTO_INCEMENT
         */
        public int getID() {
            return id;
        }
    }
}