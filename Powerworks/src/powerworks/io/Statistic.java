package powerworks.io;


public enum Statistic {
    FPS("Frames per second"), UPS("Updates per second"),
    OVERLAY_CLEAR("Time to clear overlay"), GET_DRAW_GRAPHICS("Time to get draw graphics"), CALC_RENDER_OFFSETS("Time to calc render bounds"), DRAW_BLOCKS_AND_TILES("Time to draw blocks and tiles"),
    DRAW_DROPPED_ITEMS("Time to draw dropped items"), DRAW_PLAYER("Time to draw player"), RENDER_OBJECTS_TO_GRAPHICS("Time to render object image to screen"), DRAW_HUD("Time to draw HUD"), DRAW_MOUSE("Time to draw mouse"),
    RENDER_OVERLAY_TO_GRAPHICS("Time to render overlay image to screen"), SHOW_BUFFER_STRAT("Time to show buffer strat"), TOTAL_RENDER_TIME("Total time to render");
    String name;
    
    private Statistic(String name) {
	this.name = name;
    }
}
