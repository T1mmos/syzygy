package gent.timmos.mvntry;

import java.awt.Graphics2D;

/**
 * Represents the actual ticking engine, updating the game state and rendering the state.
 * @author Timmos
 */
public interface Engine {

    public void initialize();
    public void updateGame(UpdateInfo info);
    
    public void renderGame(Graphics2D g, RenderInfo info);
}
