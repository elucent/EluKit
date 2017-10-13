package elukit.client.texture;

public class Texture {
	int width, height;
	private int texid;
	public Texture(int width, int height, int texid){
		this.width = width;
		this.height = height;
		this.texid = texid;
	}
	public int getId() {
		return texid;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
}
