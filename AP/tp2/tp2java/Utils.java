package tp2java;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

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
}
