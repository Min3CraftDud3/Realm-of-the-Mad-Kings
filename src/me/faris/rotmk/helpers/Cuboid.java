package me.faris.rotmk.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Cuboid implements Iterable<Block>, Cloneable {
	protected String worldName;
	protected final int xPos1, yPos1, zPos1;
	protected final int xPos2, yPos2, zPos2;

	public Cuboid(Location loc) {
		this(loc, loc);
	}

	public Cuboid(Cuboid cuboid) {
		this(cuboid.getWorld(), cuboid.xPos1, cuboid.yPos1, cuboid.zPos1, cuboid.xPos2, cuboid.yPos2, cuboid.zPos2);
	}

	public Cuboid(Location loc1, Location loc2) {
		if (loc1 != null && loc2 != null) {
			if (loc1.getWorld() != null && loc2.getWorld() != null) {
				if (!loc1.getWorld().equals(loc2.getWorld())) throw new IllegalStateException("The 2 locations of the cuboid must be in the same world!");
			}
			this.worldName = loc1.getWorld() != null ? loc1.getWorld().getName() : "";
			this.xPos1 = Math.min(loc1.getBlockX(), loc2.getBlockX());
			this.yPos1 = Math.min(loc1.getBlockY(), loc2.getBlockY());
			this.zPos1 = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
			this.xPos2 = Math.max(loc1.getBlockX(), loc2.getBlockX());
			this.yPos2 = Math.max(loc1.getBlockY(), loc2.getBlockY());
			this.zPos2 = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
		} else {
			this.worldName = "";
			this.xPos1 = 0;
			this.yPos1 = 0;
			this.zPos1 = 0;
			this.xPos2 = 0;
			this.yPos2 = 0;
			this.zPos2 = 0;
		}
	}

	public Cuboid(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		this.worldName = world.getName();
		this.xPos1 = Math.min(x1, x2);
		this.xPos2 = Math.max(x1, x2);
		this.yPos1 = Math.min(y1, y2);
		this.yPos2 = Math.max(y1, y2);
		this.zPos1 = Math.min(z1, z2);
		this.zPos2 = Math.max(z1, z2);
	}

	public Cuboid(Map<String, Object> map) {
		this.worldName = (String) map.get("worldName");
		this.xPos1 = (Integer) map.get("x1");
		this.xPos2 = (Integer) map.get("x2");
		this.yPos1 = (Integer) map.get("y1");
		this.yPos2 = (Integer) map.get("y2");
		this.zPos1 = (Integer) map.get("z1");
		this.zPos2 = (Integer) map.get("z2");
	}

	public boolean contains(Location location) {
		int lX = (int) location.getX();
		int lY = (int) location.getY();
		int lZ = (int) location.getZ();
		return lX >= this.xPos1 && lX <= this.xPos2 && lY >= this.yPos1 && lY <= this.yPos2 && lZ >= this.zPos1 && lZ <= this.zPos2;
	}

	public List<Block> getBlocks() {
		List<Block> blockList = new ArrayList<Block>();
		World world = this.getWorld();
		if (world != null) {
			for (int x = this.xPos1; x <= this.xPos2; x++) {
				for (int y = this.yPos1; y <= this.yPos2; y++) {
					for (int z = this.zPos1; z <= this.zPos2; z++) {
						blockList.add(world.getBlockAt(x, y, z));
					}
				}
			}
			return blockList;
		} else {
			return new ArrayList<Block>();
		}
	}

	public int getLowerX() {
		return this.xPos1;
	}

	public int getLowerY() {
		return this.yPos1;
	}

	public int getLowerZ() {
		return this.zPos1;
	}

	public int getUpperX() {
		return this.xPos2;
	}

	public int getUpperY() {
		return this.yPos2;
	}

	public int getUpperZ() {
		return this.zPos2;
	}

	public int getVolume() {
		return (this.xPos2 - this.xPos1 + 1) * (this.yPos2 - this.yPos1 + 1) * (this.zPos2 - this.zPos1 + 1);
	}

	public Location getLowerLocation() {
		return new Location(this.getWorld(), this.xPos1, this.yPos1, this.zPos1);
	}

	public Location getUpperLocation() {
		return new Location(this.getWorld(), this.xPos2, this.yPos2, this.zPos2);
	}

	public World getWorld() {
		World world = Bukkit.getWorld(this.worldName);
		if (world == null) throw new IllegalStateException("World '" + this.worldName + "' is not loaded");
		return world;
	}

	public void setWorld(World world) {
		if (world != null) this.worldName = world.getName();
	}

	@Override
	public Iterator<Block> iterator() {
		return this.getBlocks().iterator();
	}

	@Override
	public Cuboid clone() {
		return new Cuboid(this);
	}

	@Override
	public String toString() {
		return new String(this.worldName + "," + this.xPos1 + "," + this.yPos1 + "," + this.zPos1 + "," + this.xPos2 + "," + this.yPos2 + "," + this.zPos2);
	}

	public static Cuboid deserialize(String serializedString) {
		try {
			String[] cuboidSplit = serializedString.split(",");
			String worldName = cuboidSplit[0];
			World world = Bukkit.getWorld(worldName);
			int xPos1 = Integer.parseInt(cuboidSplit[1]), yPos1 = Integer.parseInt(cuboidSplit[2]), zPos1 = Integer.parseInt(cuboidSplit[3]);
			int xPos2 = Integer.parseInt(cuboidSplit[4]), yPos2 = Integer.parseInt(cuboidSplit[5]), zPos2 = Integer.parseInt(cuboidSplit[6]);
			Location loc1 = new Location(world, xPos1, yPos1, zPos1);
			Location loc2 = new Location(world, xPos2, yPos2, zPos2);
			return new Cuboid(loc1, loc2);
		} catch (Exception ex) {
			return new Cuboid(Bukkit.getWorlds().get(0).getSpawnLocation());
		}
	}
}
