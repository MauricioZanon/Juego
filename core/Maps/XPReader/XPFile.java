package XPReader;

import java.util.ArrayList;

/**
 * Created by bison on 02-01-2016.
 */
public class XPFile {
    int version;
    int noLayers;
    public ArrayList<XPLayer> layers;

    public XPFile(int version, int noLayers, ArrayList<XPLayer> layers) {
        this.version = version;
        this.noLayers = noLayers;
        this.layers = layers;
    }

    public XPLayer layer(int i){
        return layers.get(i);
    }

    public int noLayers(){
        return noLayers;
    }
}
