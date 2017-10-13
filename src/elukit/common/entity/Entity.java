package elukit.common.entity;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import elukit.client.Main;
import elukit.common.collider.ICollider;
import elukit.common.level.Chunk;
import elukit.common.level.World;
import elukit.common.util.DataHelper;

public class Entity {
	public float x = 0, y = 0, z = 0;
	public float px = 0, py = 0, pz = 0;
	public float motionX = 0, motionY = 0, motionZ = 0;
	public float width = 0, height = 0;
	public boolean noClip = false;
	public boolean grounded = false;
	public boolean collidedLeft, collidedRight, collidedUp, collidedDown, collidedIn, collidedOut;
	public World world;
	public float timeExisted = 0;
	float xFaceCoeff = 0;
	float zFaceCoeff = 0;
	public int id = 0;
	public boolean alive = true;
	
	public void update(){
		collidedLeft = false;
		collidedRight = false;
		collidedUp = false;
		collidedDown = false;
		collidedIn = false;
		collidedOut = false;
		grounded = false;
		px = x;
		py = y;
		pz = z;
		Main.gameRenderer.pause();
		x += motionX*Main.deltaTime;
		y += motionY*Main.deltaTime;
		z += motionZ*Main.deltaTime;
		if (!noClip){
			int ix = (int)Math.floor(x);
			int iy = (int)Math.floor(y);
			int iz = (int)Math.floor(z);
			List<ICollider> colliders = new ArrayList<>();
			for (int i = -3; i < 4; i ++){
				for (int j = -3; j < 4; j ++){
					for (int k = -3; k < 4; k ++){
						Main.world.addColliders(colliders, ix+i,iy+j,iz+k);
					}
				}
			}
			for (ICollider c : colliders){
				doCollision(c);
			}
		}
		Main.gameRenderer.resume();
		timeExisted += Main.deltaTime;
		if (timeExisted > 92160f){
			timeExisted -= 92160f;
		}
	}
	
	public void doCollision(ICollider c){
		if (!noClip){
			if (c.isInside(x-width/3.0f, y+height, z-width/3.0f)){
				if (motionY > 0){
					motionY = 0;
				}
				this.y = c.getBottom(x-width/3.0f,y+height,z-width/3.0f)-height;
				collidedUp = true;
			}
			else if (c.isInside(x+width/3.0f, y+height, z-width/3.0f)){
				if (motionY > 0){
					motionY = 0;
				}
				this.y = c.getBottom(x+width/3.0f,y+height,z-width/3.0f)-height;
				collidedUp = true;
			}
			else if (c.isInside(x+width/3.0f, y+height, z+width/3.0f)){
				if (motionY > 0){
					motionY = 0;
				}
				this.y = c.getBottom(x+width/3.0f,y+height,z+width/3.0f)-height;
				collidedUp = true;
			}
			else if (c.isInside(x-width/3.0f, y+height, z+width/3.0f)){
				if (motionY > 0){
					motionY = 0;
				}
				this.y = c.getBottom(x-width/3.0f,y+height,z+width/3.0f)-height;
				collidedUp = true;
			}
			
			if (c.isInside(x-width/4.0f, y, z)){
				if (motionY < 0){
					motionY = 0;
				}
				this.y = c.getTop(x-width/4.0f, y, z);
				collidedDown = true;
			}
			else if (c.isInside(x+width/4.0f, y, z)){
				if (motionY < 0){
					motionY = 0;
				}
				this.y = c.getTop(x+width/4.0f, y, z);
				collidedDown = true;
			}
			else if (c.isInside(x, y, z-width/4.0f)){
				if (motionY < 0){
					motionY = 0;
				}
				this.y = c.getTop(x, y, z-width/4.0f);
				collidedDown = true;
			}
			else if (c.isInside(x, y, z+width/4.0f)){
				if (motionY < 0){
					motionY = 0;
				}
				this.y = c.getTop(x, y, z+width/4.0f);
				collidedDown = true;
			}
			
			if (c.isInside(x-width/3.0f, y-height/6.0f, z-width/3.0f)){
				grounded = true;
			}
			else if (c.isInside(x+width/3.0f, y-height/6.0f, z-width/3.0f)){
				grounded = true;
			}
			else if (c.isInside(x+width/3.0f, y-height/6.0f, z+width/3.0f)){
				grounded = true;
			}
			else if (c.isInside(x-width/3.0f, y-height/6.0f, z+width/3.0f)){
				grounded = true;
			}
			
			
			float horizHeight = 0.75f;
			if (c.isInside(x+width/2.0f, y+horizHeight*height/4.0f, z)){
				motionX = 0;
				this.x = c.getWest(x+width/2.0f, y+horizHeight*height/4.0f, z)-width/2.0f;
				collidedRight = true;
			}
			if (c.isInside(x-width/2.0f, y+horizHeight*height/4.0f, z)){
				motionX = 0;
				this.x = c.getEast(x-width/2.0f, y+horizHeight*height/4.0f, z)+width/2.0f;
				collidedLeft = true;
			}
			if (c.isInside(x, y+horizHeight*height/4.0f, z+width/2.0f)){
				motionZ = 0;
				this.z = c.getNorth(x, y+horizHeight*height/4.0f, z+width/2.0f)-width/2.0f;
				collidedOut = true;
			}
			if (c.isInside(x, y+horizHeight*height/4.0f, z-width/2.0f)){
				motionZ = 0;
				this.z = c.getSouth(x, y+horizHeight*height/4.0f, z-width/2.0f)+width/2.0f;
				collidedIn = true;
			}
			if (c.isInside(x+width/2.0f, y+(4f-horizHeight)*height/4.0f, z)){
				motionX = 0;
				this.x = c.getWest(x+width/2.0f, y+(4f-horizHeight)*height/4.0f, z)-width/2.0f;
				collidedRight = true;
			}
			if (c.isInside(x-width/2.0f, y+(4f-horizHeight)*height/4.0f, z)){
				motionX = 0;
				this.x = c.getEast(x-width/2.0f, y+(4f-horizHeight)*height/4.0f, z)+width/2.0f;
				collidedLeft = true;
			}
			if (c.isInside(x, y+(4f-horizHeight)*height/4.0f, z+width/2.0f)){
				motionZ = 0;
				this.z = c.getNorth(x, y+(4f-horizHeight)*height/4.0f, z+width/2.0f)-width/2.0f;
				collidedOut = true;
			}
			if (c.isInside(x, y+(4f-horizHeight)*height/4.0f, z-width/2.0f)){
				motionZ = 0;
				this.z = c.getSouth(x, y+(4f-horizHeight)*height/4.0f, z-width/2.0f)+width/2.0f;
				collidedIn = true;
			}
		}
	}
	
	public void setLevel(World level){
		this.world = level;
	}
	
	public void render(){
		
	}
	
	public byte[] writeData(){
		return DataHelper.serialize(x,y,z);
	}
	
	public void readData(byte[] data){
		List<Object> objects = DataHelper.deserialize(data);
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
