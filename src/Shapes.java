import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.*;

/**
 * Use JOGL to draw a simple cube with each face being a different color.
 * Rotations can be applied with the arrow keys, the page up key, and the page
 * down key. The home key will set all rotations to 0. Initial rotations about
 * the x, y, and z axes are 15, -15, and 0.
 *
 * This program is meant as an example of using modeling transforms, with
 * glPushMatrix and glPopMatrix.
 *
 * Note that this program does not use lighting.
 */
public class Shapes extends GLJPanel implements GLEventListener, KeyListener {

	/**
	 * A main routine to create and show a window that contains a panel of type
	 * UnlitCube. The program ends when the user closes the window.
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("A Simple Unlit Cube -- ARROW KEYS ROTATE");
		Shapes panel = new Shapes();
		window.setContentPane(panel);
		window.pack();
		window.setLocation(50, 50);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		panel.requestFocusInWindow();
	}

	/**
	 * Constructor for class UnlitCube.
	 */
	public Shapes() {
		super(new GLCapabilities(null)); // Makes a panel with default OpenGL "capabilities".
		setPreferredSize(new Dimension(500, 500));
		addGLEventListener(this); // A listener is essential! The listener is where the OpenGL programming lives.
		addKeyListener(this);
	}

	// -------------------- methods to draw the cube ----------------------

	double rotateX = 15; // rotations of the cube about the axes
	double rotateY = -15;
	double rotateZ = 0;
	double translateX = 0;
	double translateY = 0;

	private void square(GL2 gl2, double r, double g, double b) {
		gl2.glColor3d(r, g, b);
		gl2.glBegin(GL2.GL_TRIANGLE_FAN);
		gl2.glVertex3d(-0.5, -0.5, 0.5);
		gl2.glVertex3d(0.5, -0.5, 0.5);
		gl2.glVertex3d(0.5, 0.5, 0.5);
		gl2.glVertex3d(-0.5, 0.5, 0.5);
		gl2.glEnd();
	}
	private void cube(GL2 gl2, double size) {
		gl2.glPushMatrix();
		gl2.glScaled(size, size, size); // scale unit cube to desired size
		// Move the squares to offset 3,3
		gl2.glTranslated(3, 3, 0);
		square(gl2, 1, 0, 0); // red front face

		gl2.glPushMatrix();
		gl2.glRotated(90, 0, 1, 0);

		square(gl2, 0, 1, 0); // green right face
		gl2.glPopMatrix();

		gl2.glPushMatrix();
		gl2.glRotated(-90, 1, 0, 0);
		square(gl2, 0, 0, 1); // blue top face
		gl2.glPopMatrix();

		gl2.glPushMatrix();
		gl2.glRotated(180, 0, 1, 0);
		square(gl2, 0, 1, 1); // cyan back face
		gl2.glPopMatrix();

		gl2.glPushMatrix();
		gl2.glRotated(-90, 0, 1, 0);
		square(gl2, 1, 0, 1); // magenta left face
		gl2.glPopMatrix();

		gl2.glPushMatrix();
		gl2.glRotated(90, 1, 0, 0);
		square(gl2, 1, 1, 0); // yellow bottom face
		gl2.glPopMatrix();

		gl2.glPopMatrix(); // Restore matrix to its state before cube() was called.
	}

	// -------------------- GLEventListener Methods -------------------------

	/**
	 * The display method is called when the panel needs to be redrawn. The is where
	 * the code goes for drawing the image, using OpenGL commands.
	 */
	public void display(GLAutoDrawable drawable) {

		GL2 gl2 = drawable.getGL().getGL2(); // The object that contains all the OpenGL methods.

		gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl2.glLoadIdentity(); // Set up modelview transform.
		gl2.glRotated(rotateZ, 0, 0, 1);
		gl2.glRotated(rotateY, 0, 1, 0);
		gl2.glRotated(rotateX, 1, 0, 0);
		gl2.glTranslated(translateX, 1, 0);
		gl2.glTranslated(translateY, 0, 1);
		

		cube(gl2, 1);

		// Add an Index Face set
		// Note using Graph paper is the best way to figure these vertices.
		// You can make about any shape you want this way
		float[][] colorList = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 }, { 1, 1, 0 }, { 1, 0, 1 }, { 0, 1, 1 } };

		double[][] houseVertexList = { { 1, -0.5, 1 }, { 1, -.5, -1 }, { 1, .5, -1 }, { 1, .5, 1 }, { .75, .75, 0 },
				{ -.75, .75, 0 }, { -1, -.5, 1 }, { -1, .5, 1 }, { -1, .5, -1 }, { -1, -.5, -1 } };
		int[][] houseFaceList = { { 0, 1, 2, 3 }, { 3, 2, 4 }, { 7, 3, 4, 5 }, { 2, 8, 5, 4 }, { 5, 8, 7 },
				{ 0, 3, 7, 6 }, { 0, 6, 9, 1 }, { 2, 1, 9, 8 }, { 6, 7, 8, 9 } };

		double[][] pyramidVertexList = { { 0, 0, 0 }, { 1, 0, 0 }, { 0, 0, 1 }, { 1, 0, 1 }, { .5, .5, .5 } };
		int[][] pyramidFaceList = { { 0, 1, 2, 3 }, { 0, 1, 4 }, { 1, 2, 4 }, { 2, 3, 4 }, { 1, 3, 4 } };

		double[][] cylinderVertexList = { { 0, 1, 1 }, { .259, 1, .967 }, { .5, 1, .867 }, { .708, 1, .708 },
				{ .867, 1, .5 }, { .967, 1, .258 }, { 1, 1, 0 }, { .967, 1, -.259 }, { .867, 1, -.5 },
				{ .708, 1, -.708 }, { .5, 1, -.867 }, { .259, 1, -.967 }, { 0, 1, -1 }, { -.259, 1, -.967 },
				{ -.5, 1, -.867 }, { -.708, 1, -.708 }, { -.867, 1, -.5 }, { -.967, 1, -.259 }, { -1, 1, 0 },
				{ -.967, 1, -.259 }, { -.867, 1, .5 }, { -.708, 1, .708 }, { -.5, 1, .867 }, { -.259, 1, .967 },
				{ 0, -1, 1 }, { .259, -1, .967 }, { .5, -1, .867 }, { .708, -1, .708 }, { .867, -1, .5 },
				{ .967, -1, .259 }, { 1, -1, 0 }, { .967, -1, -.259 }, { .867, -1, -.5 }, { .708, -1, -.708 },
				{ .5, -1, -.867 }, { .259, -1, -.967 }, { 0, -1, -1 }, { -.259, -1, -.967 }, { -.5, -1, -.867 },
				{ -.708, -1, -.708 }, { -.867, -1, -.5 }, { -.967, -1, -.259 }, { -1, -1, 0 }, { -.967, -1, -.259 },
				{ -.867, -1, .5 }, { -.708, -1, .708 }, { -.5, -1, .867 }, { -.259, -1, .967 } };

		int[][] cylinderFaceList = { { 0, 1, 24, 25 }, { 1, 2, 25, 26 }, { 2, 3, 26, 27 }, { 3, 4, 27, 28 },
				{ 4, 5, 28, 29 }, { 5, 6, 29, 30 }, { 6, 7, 30, 31 }, { 7, 8, 31, 32 }, { 8, 9, 32, 33 },
				{ 9, 10, 33, 34 }, { 10, 11, 34, 35 }, { 11, 12, 35, 36 }, { 12, 13, 36, 37 }, { 13, 14, 37, 38 },
				{ 14, 15, 38, 39 }, { 15, 16, 39, 40 }, { 16, 17, 40, 41 }, { 17, 18, 41, 42 }, { 18, 19, 42, 43 },
				{ 19, 20, 43, 44 }, { 20, 21, 44, 45 }, { 21, 22, 45, 46 }, { 22, 23, 46, 47 }, { 23, 1, 47, 24 },
				{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0 },
				{ 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 24 } };

		double[][] coneVertexList = { { 0, 0, 1 }, { .259, 0, .967 }, { .5, 0, .867 }, { .708, 0, .708 },
				{ .867, 0, .5 }, { .967, 0, .258 }, { 1, 0, 0 }, { .967, 0, -.259 }, { .867, 0, -.5 },
				{ .708, 0, -.708 }, { .5, 0, -.867 }, { .259, 0, -.967 }, { 0, 0, -1 }, { -.259, 0, -.967 },
				{ -.5, 0, -.867 }, { -.708, 0, -.708 }, { -.867, 0, -.5 }, { -.967, 0, -.259 }, { -1, 0, 0 },
				{ -.967, 0, -.259 }, { -.867, 0, .5 }, { -.708, 0, .708 }, { -.5, 0, .867 }, { -.259, 0, .967 },
				{ 0, 1, 0 } };

		int[][] coneFaceList = {
				{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0 },
				{ 0, 1, 24 }, { 1, 2, 24 }, { 2, 3, 24 }, { 3, 4, 24 }, { 4, 5, 24 }, { 5, 6, 24 }, { 6, 7, 24 },
				{ 7, 8, 24 }, { 8, 9, 24 }, { 9, 10, 24 }, { 10, 11, 24 }, { 11, 12, 24 }, { 12, 13, 24 },
				{ 13, 14, 24 }, { 14, 15, 24 }, { 15, 16, 24 }, { 16, 17, 24 }, { 17, 18, 24 }, { 18, 19, 24 },
				{ 19, 20, 24 }, { 20, 21, 24 }, { 21, 22, 24 }, { 22, 23, 24 }, { 23, 0, 24 } };

		double[][] prismVertexList = { { .5, 1, -.5 }, { -.5, 1, -.5 }, { 0, 1, .5 }, { .5, -1, -.5 }, { -.5, -1, -.5 },
				{ 0, -1, .5 } };
		int[][] prismFaceList = { { 0, 1, 3, 4 }, { 1, 2, 4, 5 }, { 2, 0, 5, 3 }, { 0, 1, 2 }, { 3, 4, 5 } };
		
		
		gl2.glScaled(2, 2, 2);
		gl2.glRotated(90, 0, 0, 1);
		gl2.glTranslated(1.5, 0.5, 1.5);
		draw(gl2, pyramidFaceList, pyramidVertexList, colorList);
		
		gl2.glScaled(.5, .5, .5);
		gl2.glRotated(90, 0, 1, 0);
		gl2.glTranslated(1.5, -2.5, 1.5);
		draw(gl2, cylinderFaceList, cylinderVertexList, colorList);
		
		gl2.glRotated(90, 1, 0, 0);
		gl2.glTranslated(-1.5, -2.5, 1.5);
		draw(gl2, coneFaceList, coneVertexList, colorList);
		
		gl2.glRotated(45, 1, 0, 1);
		gl2.glTranslated(1.5, -1, 0);
		draw(gl2, prismFaceList, prismVertexList, colorList);
		
		gl2.glRotated(45, 1, 1, 1);
		gl2.glTranslated(0, -2.5, 1);
		draw(gl2, houseFaceList, houseVertexList, colorList);

	} // end display()

	public void draw(GL2 gl2, int[][] faceList, double[][] vertexList, float[][] colorList) {
		for (int i = 0; i < faceList.length; i++) {
			gl2.glColor3f(colorList[i % 6][0], colorList[i % 6][1], colorList[i % 6][2]); // Set color for face number
																							// i.
			gl2.glBegin(GL2.GL_TRIANGLE_FAN);
			for (int j = 0; j < faceList[i].length; j++) {
				int vertexNum = faceList[i][j]; // Index for vertex j of face i.
				double[] vertexCoords = vertexList[vertexNum]; // The vertex itself.
				gl2.glVertex3dv(vertexCoords, 0);
			}
			gl2.glEnd();
		}
	}

	public void init(GLAutoDrawable drawable) {
		// called when the panel is created
		GL2 gl2 = drawable.getGL().getGL2();
		gl2.glMatrixMode(GL2.GL_PROJECTION);
		// gl2.glOrtho(-1, 1 ,-1, 1, -1, 1);
		// Changing this is your coordinate -x,x,-y,y,-z,z)
		// Larger numbers zooms out.
		// Play with this to make sure you see your shapes.
		gl2.glOrtho(-10, 10, -10, 10, -10, 10);
		gl2.glMatrixMode(GL2.GL_MODELVIEW);
		gl2.glClearColor(0, 0, 0, 1);
		gl2.glEnable(GL2.GL_DEPTH_TEST);
	}

	public void dispose(GLAutoDrawable drawable) {
		// called when the panel is being disposed
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// called when user resizes the window
	}

	// ---------------- Methods from the KeyListener interface --------------

	public void keyPressed(KeyEvent evt) {
		int key = evt.getKeyCode();
		if (key == KeyEvent.VK_LEFT)
			rotateY -= 15;
		else if (key == KeyEvent.VK_RIGHT)
			rotateY += 15;
		else if (key == KeyEvent.VK_DOWN)
			rotateX += 15;
		else if (key == KeyEvent.VK_UP)
			rotateX -= 15;
		else if (key == KeyEvent.VK_PAGE_UP)
			rotateZ += 15;
		else if (key == KeyEvent.VK_PAGE_DOWN)
			rotateZ -= 15;
		else if (key == KeyEvent.VK_HOME)
			rotateX = rotateY = rotateZ = 0;
		else if(key == KeyEvent.VK_W) {
			translateX += 15;
		}
		repaint();
	}

	public void keyReleased(KeyEvent evt) {
	}

	public void keyTyped(KeyEvent evt) {
	}

}
