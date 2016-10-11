package gent.timdemey.syzygy.raycast.render.twod;

import gent.timdemey.syzygy.core.Frame;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.render.RCRenderer;
import gent.timdemey.syzygy.raycast.render.twod.entity.BackgroundEntity;
import gent.timdemey.syzygy.raycast.render.twod.entity.GridEntity;
import gent.timdemey.syzygy.raycast.render.twod.entity.HitwallEntity;
import gent.timdemey.syzygy.raycast.render.twod.entity.PlayerEntity;
import gent.timdemey.syzygy.raycast.render.twod.entity.RenderEntity;
import gent.timdemey.syzygy.raycast.render.twod.entity.TextEntity;
import gent.timdemey.syzygy.raycast.render.twod.entity.WallpointsEntity;
import gent.timdemey.syzygy.raycast.render.twod.entity.WallsEntity;
import gent.timdemey.syzygy.raycast.world.GameState;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

/**
 * Renders the internal world in a topdown view. This can be used a
 * @author Timmos
 */
public class RC2DRenderer implements RCRenderer {

    private final RC2DRenderInfo ri;

    private final RenderEntity[] entities_bg = new RenderEntity[] {
                    new BackgroundEntity()
    };

    private final RenderEntity[] entities_world  = new RenderEntity[] {
                    new GridEntity(),
                    new WallsEntity(),
                    new WallpointsEntity(), new HitwallEntity(),

                    new PlayerEntity(),
    };

    private final RenderEntity[] entities_flat  = new RenderEntity[] {
                    new TextEntity()
    };

    public RC2DRenderer() {
        ri = new RC2DRenderInfo();
    }

    @Override
    public void renderAll(Graphics2D g, Frame fInfo, RenderInfo rInfo, GameState sInfo) {
        calc(fInfo, rInfo, sInfo);

        render(entities_bg, g, fInfo, rInfo, sInfo);

        setRenderingHints(g);
        AffineTransform origT = g.getTransform();
        // put the world in the middle of the screen
        g.translate((rInfo.resx - ri.gridW) / 2, -(rInfo.resy - ri.gridH) / 2);
        render(entities_world, g, fInfo, rInfo, sInfo);

        // reset translation, render flat stuff
        g.setTransform(origT);
        render(entities_flat, g, fInfo, rInfo, sInfo);
    }

    private void calc(Frame fInfo, RenderInfo rInfo, GameState sInfo) {
        ri.wallW = (rInfo.resx - 1) / (sInfo.map.walls_x + 2); // keep a border of 1 wall width on both sides
        ri.wallH = (rInfo.resy - 1) / (sInfo.map.walls_y + 2); // same
        ri.gridW = ri.wallW * sInfo.map.walls_x + 1;
        ri.gridH = ri.wallH * sInfo.map.walls_y + 1;
    }

    private static void setRenderingHints(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
    }

    private void render (RenderEntity[] entities, Graphics2D g, Frame fInfo, RenderInfo rInfo, GameState sInfo) {
        for (int i = 0; i < entities.length; i++) {
            entities[i].render(g, fInfo, rInfo, sInfo, ri);
        }
    }
}
