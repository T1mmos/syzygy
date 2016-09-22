package gent.timdemey.syzygy.core;

import java.awt.Graphics2D;

/**
 * Represents the actual ticking engine, updating the game state and rendering the state.
 * @author Timmos
 */
public interface Engine {

    /**
     * Initializes this engine.
     */
    public void initialize();

    /**
     * Update the internal state of the world, using the game logic,
     * time difference, user input and other inputs.
     * @param info frame info such as time difference, fps, current user input, ...
     */
    public void updateState(FrameInfo fInfo);

    /**
     * Render the internal world representation on the screen.
     * @param g the graphics environment to render on
     * @param info frame info such as frame width and height, ...
     */
    public void renderGame(Graphics2D g, FrameInfo fInfo, RenderInfo rInfo);
}
