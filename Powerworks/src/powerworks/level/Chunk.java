package powerworks.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import powerworks.exception.NoSuchTileException;
import powerworks.level.tile.Tile;
import powerworks.level.tile.TileType;
import powerworks.main.Game;

public class Chunk {
    int xTile, yTile;
    Tile[] tiles = new Tile[64];
    public static Chunk deserialize(InputStream f) {
	BufferedReader reader = new BufferedReader(new InputStreamReader(f));
	Chunk c = new Chunk();
	String line;
	try {
	    if((line = reader.readLine()) != null) {
	        c.xTile = Integer.parseInt(line.substring(0, line.indexOf(",")));
	        c.yTile = Integer.parseInt(line.substring(line.indexOf(",") + 1));
	    }
	    if((line = reader.readLine()) != null) {
	        String[] split = line.split(" ");
	        if(split.length != 64) {
	            Game.logger.error("Invalid chunk file " + f + ": length is not right");
	            throw new IOException("Invalid chunk file " + f);
	        }
	        for(int i = 0; i < 64; i++) {
	            int xTile = i & 15;
	            c.tiles[i] = new Tile(TileType.getTileType(Integer.parseInt(split[i])), xTile + c.xTile, (i - xTile) / 16 + c.yTile);
	        }
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	} catch (NoSuchTileException e) {
	    e.printStackTrace();
	}
	return c;
    }
}
