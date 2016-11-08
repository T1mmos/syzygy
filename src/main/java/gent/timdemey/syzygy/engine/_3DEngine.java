package gent.timdemey.syzygy.engine;

import java.awt.Graphics2D;

import gent.timdemey.syzygy.core.Engine;
import gent.timdemey.syzygy.core.Frame;
import gent.timdemey.syzygy.core.RenderInfo;

public class _3DEngine implements Engine {

	private final Camera cam;
	
	public _3DEngine (){
		this.cam = new Camera();
	}
	
	@Override
	public void initialize() {
		
	}

	@Override
	public void updateState(Frame fInfo) {
		
	}

	@Override
	public void renderGame(Graphics2D g, Frame fInfo, RenderInfo rInfo) {
		}

}
