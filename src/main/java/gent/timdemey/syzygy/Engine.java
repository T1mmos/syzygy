package gent.timdemey.syzygy;

import java.awt.Graphics2D;

/**
 * Represents the actual ticking engine, updating the game state and rendering the state.
 * @author Timmos
 */
public interface Engine {

    public void initialize();

    public void updateGame(FrameInfo info);

    public void renderGame(Graphics2D g, FrameInfo info);
}
