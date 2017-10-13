package elukit.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import elukit.client.shader.ShaderManager;
import elukit.common.util.Primitives;
import elukit.common.util.RenderUtil;

public class Console {
	public List<String> text = new ArrayList<>();
	public int maxLines = 0;
	
	public Console(int max){
		this.maxLines = max;
	}
	
	public void addText(Object s){
		text.add(0, ""+s);
		if (text.size() > maxLines){
			text = text.subList(0, maxLines);
		}
	}
	
	public void render(){
		GL20.glUseProgram(0);
		for (int i = 0; i < text.size(); i ++){
			GL11.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
			RenderUtil.drawStringScaled(4, (Main.HEIGHT-20)-20*i, 2f, text.get(i));
			GL11.glColor4f(1f, 1f, 1f, 1f);
			RenderUtil.drawStringScaled(2, (Main.HEIGHT-22)-20*i, 2f, text.get(i));
		}
	}
}
