package graphics;

import static org.lwjgl.opengl.GL11.*;
//import org.lwjgl.stb.STBImage;

abstract class Tile extends GameObject {
	
	private float x, y, width, height;
	
	public Tile(float x, float y, float width, float height) {
		this.offsetX = x;
		this.offsetY = y;
		this.width = width;
		this.height = height;
	}
	
	public static void DrawQuad(float x, float y, float width, float height) {
		glBegin(GL_QUADS);
		glVertex2f(x, y);
		glVertex2f(x + width, y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
		glEnd();
	}
	
//	public static void DrawQuadTex(STBImage tex, float x, float y, float width, float height) {
//	tex.bind();
//	glTranslatef(x,y,0f);
//	glBegin(GL_QUADS);
//	glTexCoord2f(0,0);
//	glVertex2f(0,0);
//}
	
	
	
//	public static Tile tiles[] = new Tile[16];
//	
//	public static final Tile test_tile = new Tile((byte) 0, "..resources/tile_texture.jpg");
//	
//	private byte id;
//	private String texture;
//	
//	public Tile(byte id, String texture) {
//		this.id = id;
//		this.texture = texture;
//		if(tiles[id] != null)
//			throw new IllegalStateException("Tiles at ["+id+"] is already being used");
//		tiles[id] = this;
//	}
//
//	public byte getId() {
//		return id;
//	}
//
//	public String getTexture() {
//		return texture;
//	}
}
