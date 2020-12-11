package graphics;

import static org.lwjgl.opengl.GL11.*;
//import org.lwjgl.stb.STBImage;

class Tile extends GameObject {
	
	private float width, height;
	
	public Tile(float posX, float posY, float width, float height) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
	}

	@Override
	public void drawGraphic() {
		glBegin(GL_QUADS);
			glColor4f(.8f,.8f,.8f,0);
			glVertex2f(posX - width/2 - offsetX, posY + height/2 - offsetY);
			glVertex2f(posX + width/2 - offsetX, posY + height/2 - offsetY);
			glVertex2f(posX + width/2 - offsetX, posY - height/2 - offsetY);
			glVertex2f(posX - width/2 - offsetX, posY - height/2 - offsetY);
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
