package tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CustomShapeRenderer extends ShapeRenderer{

	public void drawMenuBox(float x, float y, float width, float height, Color borderColor) {
		int layers = 10;
		Color bc = new Color(borderColor);
		for(int i = 0; i <= layers; i++) {
			int l = layers - i;
			setColor(bc.lerp(Color.BLACK, (float)i/(float)layers));
			roundedRect(x-l, y-l, width+l*2, height+l*2, 7f);
		}
	}
	
	public void roundedRect(float x, float y, float width, float height, float radius){
		rect(x + radius, y + radius, width - 2*radius, height - 2*radius);
		
		rect(x + radius, y, width - 2*radius, radius);
		rect(x + width - radius, y + radius, radius, height - 2*radius);
		rect(x + radius, y + height - radius, width - 2*radius, radius);
		rect(x, y + radius, radius, height - 2*radius);
		
		arc(x + radius, y + radius, radius, 180f, 90f);
		arc(x + width - radius, y + radius, radius, 270f, 90f);
		arc(x + width - radius, y + height - radius, radius, 0f, 90f);
		arc(x + radius, y + height - radius, radius, 90f, 90f);
    }
}
