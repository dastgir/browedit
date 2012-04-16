package com.exnw.browedit.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import javax.media.opengl.GL4;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;

import com.exnw.browedit.grflib.GrfLib;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureData;

public class TextureCache
{
	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	
	public static Texture getTexture(GL4 gl, String fileName)
	{
		Texture t = TextureCache.textures.get( fileName );
		
		if( t == null )
		{
			try
			{
				//TODO: can't we do this quicker?
				BufferedImage img = ImageIO.read(GrfLib.openFile(fileName));
				if(img == null)
					return null;
				BufferedImage dest = new BufferedImage(
					    img.getWidth(), img.getHeight(),
					    BufferedImage.TYPE_INT_ARGB);
					
				for(int x = 0; x < img.getWidth(); x++)
				{
					for(int y = 0; y < img.getHeight(); y++)
					{
						int rgb = img.getRGB(x, y);
						if((rgb & 0xFF) >= 246 && ((rgb>>16)&0xFF) >= 246 && ((rgb>>8)&0xFF) <= 10)
							rgb = 0x00000000;
						dest.setRGB(x, y, rgb);
					}
				}
				
				t = TextureIO.newTexture(new AWTTextureData(gl.getGLProfile(), 0,0,true,dest));
				t.setTexParameteri(gl, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);				
//				Texture t = TextureIO.newTexture(GrfLib.openFile(fileName), true, fileName.substring(fileName.lastIndexOf('.')));
				textures.put(fileName, t);
			} catch (GLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return t;
	}
}
