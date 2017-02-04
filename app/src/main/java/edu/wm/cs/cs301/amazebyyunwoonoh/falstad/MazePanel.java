package edu.wm.cs.cs301.amazebyyunwoonoh.falstad;



//import falstad.Constants.StateGUI;
//import falstad.RangeSet.RangeSetElement;
//import generation.Seg;

import edu.wm.cs.cs301.amazebyyunwoonoh.ui.PlayActivity;
import edu.wm.cs.cs301.amazebyyunwoonoh.ui.RobotActivity;
import android.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;






/**
 * Add functionality for double buffering to an AWT Panel class.
 * Used for drawing a maze.
 * 
 * @author pk
 *
 */
public class MazePanel {
	/* Panel operates a double buffer see
	 * http://www.codeproject.com/Articles/2136/Double-buffer-in-standard-Java-AWT
	 * for details
	 */
	//private Image bufferImage ;
	//refactor fields 
	//private Graphics2D gc;
	//private Color color;
	//private Point p;




    private Canvas canvas;
    private Paint color = new Paint();
    private PlayActivity manualAct;
    private RobotActivity autoAct;
    private int colorInt;
    private boolean manual = false;

    private MazeController controller;
    private Seg seg;

    private Bitmap sky;
    private Bitmap floor;
    private Bitmap wall;
	
	
	/**
	 * Constructor. Object is not focusable.
	 */
	public MazePanel() {
		super() ;
		//this.setFocusable(false) ;
	}
	
	public MazePanel(MazeController mazeController) {
		super();
		controller = mazeController;
		//this.setFocusable(false);
	}
	
	public MazePanel(Seg seg){
		super();
		this.seg = seg;
		//this.setFocusable(false);
	}
	
	
    /*
	@Override
	public void update(Graphics g) {
		paint(g) ;
	}
	public void update() {
		paint(getGraphics()) ;
	}
	*/

	/**
	 * Draws the buffer image to the given graphics object.
	 * This method is called when this panel should redraw itself.
	 */
    /*
	@Override
	public void paint(Graphics g) {
		if (null == g) {
			System.out.println("MazePanel.paint: no graphics object, skipping drawImage operation") ;
		}
		else {
			g.drawImage(bufferImage,0,0,null) ;	
		}
	}

	public void initBufferImage() {
		bufferImage = createImage(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		if (null == bufferImage)
		{
			System.out.println("Error: creation of buffered image failed, presumedly container not displayable");
		}
	}
	*/
	/**
	 * Obtains a graphics object that can be used for drawing. 
	 * Multiple calls to the method will return the same graphics object 
	 * such that drawing operations can be performed in a piecemeal manner 
	 * and accumulate. To make the drawing visible on screen, one
	 * needs to trigger a call of the paint method, which happens 
	 * when calling the update method. 
	 * @return graphics object to draw on
	 */
    /*
	public Graphics getBufferGraphics() {
		if (null == bufferImage)
			initBufferImage() ;
		if (null == bufferImage)
			return null ;
		return bufferImage.getGraphics() ;
	}
    */
	
	/**
	 * createPanel creates panel that contains combo boxes and button
	 */
	/*
	public void createPanel() {
		JPanel newPanel = new JPanel(new GridBagLayout());
		setSize(400,420);
		String[] driver = {"Manual", "Wizard", "WallFollower", "Pledge"};
		String[] mazeBuilder = {"DFS", "Prim", "Eller"};
		String[] difficulty = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e"};
		
		JComboBox<String> box1 = new JComboBox<>(driver);
		JComboBox<String> box2 = new JComboBox<>(mazeBuilder);
		JComboBox<String> box3 = new JComboBox<>(difficulty);
		JButton button = new JButton("START");
		
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				controller.setDriver(box1.getSelectedItem().toString());
				controller.setMazeBuilder(box2.getSelectedItem().toString());
				controller.setDifficulty(box3.getSelectedItem().toString());
				controller.switchToGeneratingScreen(1);
			}
		});
		//setLayout(new FlowLayout());
		//box1.setSelectedIndex(0);
		newPanel.add(box1);
		newPanel.add(box2);
		newPanel.add(box3);
		newPanel.add(button);
		add(newPanel, BorderLayout.SOUTH);
		newPanel.setVisible(true);
		
		
	}
	*/
	/**
	 * From FirstPersonDrawer
	 * became necessary when lines of polygons that were not horizontal or vertical looked ragged
	 */
    /*
	public void create2DGraphics() {
		gc = (Graphics2D) getBufferGraphics();
		gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		gc.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	}
	*/
	/**
	 * set color according to string value 
	 * @param string
	 */
	public void coloring(String string) {
		if (string == "black"){
            colorInt = Color.BLACK;
			//gc.setColor(Color.black);
		}
		if (string == "white"){
            colorInt = Color.WHITE;
			//gc.setColor(Color.white);
		}
		if (string == "darkGray"){
            colorInt = Color.DKGRAY;
			//gc.setColor(Color.darkGray);
		}
		if (string == "red"){
            colorInt = Color.RED;
			//gc.setColor(Color.red);
		}
		if (string == "yellow"){
            colorInt = Color.YELLOW;
			//gc.setColor(Color.yellow);
		}
		if (string == "gray"){
            colorInt = Color.GRAY;
			//gc.setColor(Color.gray);
		}

        color.setColor(colorInt);

	}

    public Paint getColor(){
        return color;
    }

	/**
	 * Color by taking integer value 
	 * @param i
	 */
	public void coloringWithColor(int i){
		color.setColor(i);
	}
	
	/**
	 * draw rectangle in the background
	 * @param i, j, k, l 
	 */
    /*
	public void filling(int i, int j, int k, int l) {
		gc.fillRect(i,j,k,l);
	}
	*/

	/**
	 * draw line with 4 int inputs 
	 * @param i, j, k, l
	 */
    /*
	public void lineDrawing(int i , int j, int k, int l) {
		gc.drawLine(i, j, k, l);
		
	}
	*/
	/**
	 * get gc
	 * gc is graphics handler for the buffer image that this class draws on
	 * @return
	 */
    /*
	public Graphics getGraphicObject(){
		return gc;
	}
	*/
	/**
	 * set gc 
	 */
    /*
	public void setGraphics() {
		gc = (Graphics2D) getBufferGraphics();
	}
	*/
    /**
	 * rgb color setter with three int parameters 
	 * @param r, g, b 
	 */
	public void RGBSetter(int r, int g, int b) {
		//color = new Color(r,g,b);
        int colorInt = (255)<<24|((r)<<16)|((g)<<8)|(b);
        color.setColor(colorInt);
	}
	/**
	 * rgb setter
	 * @return
	 */
	public Paint RGBGetter(){
		return color;
	}
	/*
	public void setPoint(int x1, int x2) {
		p = new Point(x1, x2);
	}
	public Point getPoint(){
		return p;
	}
	*/
	/**
	 * gc setter with color as parameter 
	 * @param color
	 */
	public void setterWithParmColor(Paint color){
		this.color = color;
        //gc = (Graphics2D) this.getBufferGraphics();
		//gc.setColor(color);
	}



    ////////////////////////////// Methods for Android //////////////////////////////

    public void setCanvas(Canvas canvas){
        this.canvas = canvas;
    }

    public Canvas getCanvas(){
        return canvas;
    }

    public void setPlayActivity(PlayActivity playAct){
        manualAct = playAct;
        sky = manualAct.getSky();
        floor = manualAct.getFloor();
    }

    public PlayActivity getPlayActivity(){
        return manualAct;
    }

    public void setRobotActivity(RobotActivity robotAct){
        autoAct = robotAct;
        sky = autoAct.getSky();
        floor = autoAct.getFloor();
    }

    public RobotActivity getRobotActivity(){
        return autoAct;
    }

    public void update(){
        if(manual){
            manualAct.runOnUiThread(new Runnable() {
                public void run() {
                    manualAct.updateView();
                }
            });
        }else{
            autoAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    autoAct.updateView();
                }
            });
        }
    }

    public void setManual(boolean manual){
        this.manual = manual;
    }


    ////////////////////////////// Drawing //////////////////////////////

    public void drawLine(int x1, int y1, int x2, int y2){
        canvas.drawLine(x1, y1, x2, y2, color);
    }

    public void fillOval(int x, int y, int width, int height){
        canvas.drawOval(new RectF(x,y, x+width, y+height), color);
    }

    public void fillRect(int x, int y, int width, int height) {
        if (color.getColor() == Color.BLACK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            canvas.drawBitmap(sky, null, new Rect(x, y, x + width, y + height), null);
        } else if (color.getColor() == Color.DKGRAY) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            canvas.drawBitmap(floor, null, new Rect(x, y, x + width, y + height), null);
        } else {
            canvas.drawRect(new Rect(x, y, x + width, y + height), color);
        }
    }

    public void fillPolygon(int[] xps, int[] yps, int j) {
        final Path p = new Path();
        p.moveTo(xps[0], yps[0]);
        for (int i = 0; i < j; i++){
            p.lineTo(xps[i], yps[i]);
        }
        p.lineTo(xps[0], yps[0]);
        Paint color = new Paint();
        color.setColor(colorInt);
        color.setStyle(Paint.Style.FILL);
        canvas.drawPath(p, color);
    }



	

}
