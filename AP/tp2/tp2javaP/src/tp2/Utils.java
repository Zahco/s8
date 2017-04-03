package tp2;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Utils {
	public static final int imWidth = 28;
	public static final int imHeight = 28;
	public static final int imSize = imWidth*imHeight;

	public static ArrayList<ArrayList<byte[]>> loadImages(String prefixPath) throws IOException {
		ArrayList<ArrayList<byte[]>> classes= new ArrayList<ArrayList<byte[]>>();
		for (int i=0; i<10; ++i) classes.add(new ArrayList<byte[]>());
		
        for (int c=0; c<10; ++c) {
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(prefixPath+c));
            int read=0;

            ArrayList<byte[]> cls=classes.get(c);
            do {
                byte[] img=new byte[imSize];
                read=is.read(img);
                if (read == imSize) cls.add(img);
            } while (read == imSize);
            is.close();
        }
		return classes;  
	};

	public static class GlyphViewer extends JComponent{
		private byte[] glyph;
		
		public void setGlyph(byte[]g ) { glyph=g;}
		
		public void paint(Graphics g){
			int height = 280;
			int width = 280;
			g.setColor(Color.black);
			g.fillRect(0,0,height,width);

			for (int j=0; j<Utils.imHeight; ++ j)
				for (int i=0; i<Utils.imWidth; ++ i) {
					int grey=255-2*glyph[j*28+i];
					g.setColor(new Color(grey, grey, grey));
					g.fillRect(i*10,j*10,9, 9); 
				}			
		}
	}

	public static void viewGlyph(byte[] glyph) {
		JFrame frmMain = new JFrame();
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMain.setSize(450, 450);
        //frmMain.getContentPane().setLayout(new GridLayout(2, 2));


        GlyphViewer gv=new GlyphViewer();
        gv.setGlyph(glyph);
        frmMain.getContentPane().add(new JPanel().add(gv));

/*        GlyphViewer gv2=new GlyphViewer();
        gv2.setGlyph(dataset.get(6).get(1026));
        frmMain.getContentPane().add(new JPanel().add(gv2));
        //frmMain.getContentPane().add(gv2);*/
        frmMain.setVisible(true);
	}

    public static ArrayList<ArrayList<LabelledData>> convertToLabelledData(ArrayList<ArrayList<byte[]>> classes) {
        ArrayList<ArrayList<LabelledData>> convert = new ArrayList<ArrayList<LabelledData>>(classes.size());
        for (int cls = 0; cls < classes.size(); ++cls) {
            convert.add(cls, new ArrayList<LabelledData>(classes.get(cls).size()));
            for (int j = 0; j < classes.get(cls).size(); ++j) {
                convert.get(cls).add(j, new LabelledData(cls, classes.get(cls).get(j)));
            }
        }
        return convert;
    }

	public static double euclideDistance(byte[] a, byte[] b) {
		double sum = 0;
		for (int i = 0; i < a.length; ++i) {
			sum += Math.pow(a[i] - b[i], 2);
		}
		return Math.sqrt(sum);
	}

    public static double euclideDistance(LabelledData a, LabelledData b) {
	    return euclideDistance(a.getGlyph(), b.getGlyph());
    }

	public static ArrayList<LabelledData> randomGlyphe(ArrayList<ArrayList<LabelledData>> classes, int n) {
        ArrayList<LabelledData> glyphes = new ArrayList<LabelledData>();
	    for (int cls = 0; cls < 10; ++cls) {
	        for (int j = 0; j < n; ++j) {
	            int glyphe = ThreadLocalRandom.current().nextInt(classes.get(cls).size());
                glyphes.add(classes.get(cls).get(glyphe));
            }
        }
        return glyphes;
    }

    public static ArrayList<ArrayList<LabelledData>> multiRandomGlyphe(ArrayList<ArrayList<byte[]>> classes, int n, int m) {
        ArrayList<ArrayList<LabelledData>> panel = convertToLabelledData(classes);
	    ArrayList<ArrayList<LabelledData>> multiRandomGlyphes = new ArrayList<ArrayList<LabelledData>>();
	    for (int i = 0; i < m; ++i) {
            ArrayList<LabelledData> randomGlyphes = randomGlyphe(panel, n);
            for (int j = 0; j < classes.size(); ++j) panel.get(i).removeAll(randomGlyphes);
            multiRandomGlyphes.add(randomGlyphes);
        }
	    return multiRandomGlyphes;
    }

    public static ArrayList<LabelledData> k_pp_voisins(LabelledData glyphe, ArrayList<LabelledData> glyphes, int k) {
	    TreeMap<Double, LabelledData> tm = new TreeMap<Double, LabelledData>();
	    for (int i = 0; i < glyphes.size(); ++i) {
            tm.put(euclideDistance(glyphe, glyphes.get(i)), glyphes.get(i));
	    }
        ArrayList<LabelledData> k_pp_voisins = new ArrayList<LabelledData>();
        for (Map.Entry<Double, LabelledData> entry : tm.entrySet()) {
            k_pp_voisins.add(entry.getValue());
            if (k_pp_voisins.size() == k) break;
        }
	    return k_pp_voisins;
    }

    public static int classe_principal(ArrayList<LabelledData> k_pp_voisins) {
	    int[] classes = new int[10];
        //Get number of glyphe by class
        for (LabelledData label : k_pp_voisins) {
	        classes[label.getCls()]++;
        }
        //Get max class
        int max = 0;
        for (int i = 0; i < classes.length; ++i) {
	        if (classes[i] > classes[max]) {
	            max = i;
            }
        }
        System.out.println("["+max+"] "+classes[max]+"/"+k_pp_voisins.size());
        return max;
    }
}
