package draw;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
* @author Nicholas Contreras
*/

public class ImageManager {
	
	private static HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	
	public static BufferedImage getGateImage(String name) {
		
		if (name == null) {
			return null;
		}
		
		if (images.containsKey(name)) {
			return images.get(name);
		} else {
			BufferedImage img;
			try {
				img = ImageIO.read(ImageManager.class.getResourceAsStream("/" + name));
				images.put(name, img);
				return img;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
