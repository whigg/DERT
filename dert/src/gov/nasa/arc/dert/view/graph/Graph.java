/**

DERT is a viewer for digital terrain models created from data collected during NASA missions.

DERT is Released in under the NASA Open Source Agreement (NOSA) found in the “LICENSE” folder where you
downloaded DERT.

DERT includes 3rd Party software. The complete copyright notice listing for DERT is:

Copyright © 2015 United States Government as represented by the Administrator of the National Aeronautics and
Space Administration.  No copyright is claimed in the United States under Title 17, U.S.Code. All Other Rights
Reserved.

Desktop Exploration of Remote Terrain (DERT) could not have been written without the aid of a number of free,
open source libraries. These libraries and their notices are listed below. Find the complete third party license
listings in the separate “DERT Third Party Licenses” pdf document found where you downloaded DERT in the
LICENSE folder.
 
JogAmp Ardor3D Continuation
Copyright © 2008-2012 Ardor Labs, Inc.
 
JogAmp
Copyright 2010 JogAmp Community. All rights reserved.
 
JOGL Portions Sun Microsystems
Copyright © 2003-2009 Sun Microsystems, Inc. All Rights Reserved.
 
JOGL Portions Silicon Graphics
Copyright © 1991-2000 Silicon Graphics, Inc.
 
Light Weight Java Gaming Library Project (LWJGL)
Copyright © 2002-2004 LWJGL Project All rights reserved.
 
Tile Rendering Library - Brian Paul 
Copyright © 1997-2005 Brian Paul. All Rights Reserved.
 
OpenKODE, EGL, OpenGL , OpenGL ES1 & ES2
Copyright © 2007-2010 The Khronos Group Inc.
 
Cg
Copyright © 2002, NVIDIA Corporation
 
Typecast - David Schweinsberg 
Copyright © 1999-2003 The Apache Software Foundation. All rights reserved.
 
PNGJ - Herman J. Gonzalez and Shawn Hartsock
Copyright © 2004 The Apache Software Foundation. All rights reserved.
 
Apache Harmony - Open Source Java SE
Copyright © 2006, 2010 The Apache Software Foundation.
 
Guava
Copyright © 2010 The Guava Authors
 
GlueGen Portions
Copyright © 2010 JogAmp Community. All rights reserved.
 
GlueGen Portions - Sun Microsystems
Copyright © 2003-2005 Sun Microsystems, Inc. All Rights Reserved.
 
SPICE
Copyright © 2003, California Institute of Technology.
U.S. Government sponsorship acknowledged.
 
LibTIFF
Copyright © 1988-1997 Sam Leffler
Copyright © 1991-1997 Silicon Graphics, Inc.
 
PROJ.4
Copyright © 2000, Frank Warmerdam

LibJPEG - Independent JPEG Group
Copyright © 1991-2018, Thomas G. Lane, Guido Vollbeding
 

Disclaimers

No Warranty: THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY KIND,
EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT LIMITED TO, ANY WARRANTY
THAT THE SUBJECT SOFTWARE WILL CONFORM TO SPECIFICATIONS, ANY IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY
WARRANTY THAT THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE. THIS AGREEMENT
DOES NOT, IN ANY MANNER, CONSTITUTE AN ENDORSEMENT BY GOVERNMENT AGENCY OR ANY
PRIOR RECIPIENT OF ANY RESULTS, RESULTING DESIGNS, HARDWARE, SOFTWARE PRODUCTS OR
ANY OTHER APPLICATIONS RESULTING FROM USE OF THE SUBJECT SOFTWARE.  FURTHER,
GOVERNMENT AGENCY DISCLAIMS ALL WARRANTIES AND LIABILITIES REGARDING THIRD-PARTY
SOFTWARE, IF PRESENT IN THE ORIGINAL SOFTWARE, AND DISTRIBUTES IT "AS IS."

Waiver and Indemnity:  RECIPIENT AGREES TO WAIVE ANY AND ALL CLAIMS AGAINST THE UNITED
STATES GOVERNMENT, ITS CONTRACTORS AND SUBCONTRACTORS, AS WELL AS ANY PRIOR
RECIPIENT.  IF RECIPIENT'S USE OF THE SUBJECT SOFTWARE RESULTS IN ANY LIABILITIES,
DEMANDS, DAMAGES, EXPENSES OR LOSSES ARISING FROM SUCH USE, INCLUDING ANY DAMAGES
FROM PRODUCTS BASED ON, OR RESULTING FROM, RECIPIENT'S USE OF THE SUBJECT SOFTWARE,
RECIPIENT SHALL INDEMNIFY AND HOLD HARMLESS THE UNITED STATES GOVERNMENT, ITS
CONTRACTORS AND SUBCONTRACTORS, AS WELL AS ANY PRIOR RECIPIENT, TO THE EXTENT
PERMITTED BY LAW.  RECIPIENT'S SOLE REMEDY FOR ANY SUCH MATTER SHALL BE THE IMMEDIATE,
UNILATERAL TERMINATION OF THIS AGREEMENT.

**/

package gov.nasa.arc.dert.view.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

/**
 * Provides the content for the GraphView
 *
 */
public class Graph {

	// Axes
	private Axes axes;

	// Dimensions
	private int width, height;

	// Vertices for graph line
	private float[] vertex;
	private int vertexCount;
	
	// Original 3D vertices
	private float[] origVertex;

	// Range
	private float xMin=0, yMin=0, xMax=100, yMax=100;

	// Colors
	private Color color, pickColor = Color.RED;

	// Buffer size and current portion used
	private int length, numPoints;

	// Buffer
	private int[] lineX, lineY;

	// Pick line
	private int[] pickX, pickY;
	private double valueX;

	// Java2D
	private Stroke stroke, defaultStroke;
	private Graphics2D oldG2d;

	/**
	 * Constructor
	 * 
	 * @param n
	 * @param color
	 */
	public Graph(int n, Color color, boolean axesEqualScale) {
		valueX = -1;
		this.color = color;
		length = n;
		stroke = new BasicStroke(1.5f);
		axes = new Axes();
		createLine(length);
		createPick();
		axes.equalScale = axesEqualScale;
		axes.setRange(0, 100, 0, 100);
	}

	/**
	 * Set the graph line color
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Render the graph
	 * 
	 * @param g2d
	 */
	public void render(Graphics2D g2d) {
		// changed graphics context
		if (g2d != oldG2d) {
			defaultStroke = g2d.getStroke();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			oldG2d = g2d;
		}
		// render axes
		axes.render(g2d);
		// render graph line
		g2d.setColor(color);
		g2d.setStroke(stroke);
		g2d.drawPolyline(lineX, lineY, numPoints);
		g2d.setStroke(defaultStroke);
		// render pick line
		if ((pickX[0] > axes.leftMargin) && (pickX[0] < (width - axes.rightMargin))) {
			g2d.setColor(pickColor);
			g2d.drawPolyline(pickX, pickY, 2);
		}
	}

	/**
	 * Get the graph coordinates at the mouse X/Y.
	 * 
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public float[] getValueAt(int mouseX, int mouseY) {
		valueX = (mouseX - axes.leftMargin) / axes.xScale + axes.xMin;
		double y = Double.NaN;
		int index = 0;
		if ((valueX >= axes.xMin) && (vertex != null)) {
			for (int i = 2; i < vertex.length; i += 2) {
				if (valueX < vertex[i]) {
					double w = (valueX - vertex[i - 2]) / (vertex[i] - vertex[i - 2]);
					y = w * (vertex[i + 1] - vertex[i - 1]) + vertex[i - 1];
					index = 3*i/2;
					break;
				}
			}
		}
		pickX[0] = mouseX;
		pickX[1] = mouseX;
		if ((valueX > axes.xMin) && (valueX < axes.xMax)) {
			return (new float[] { (float) valueX, (float) y, origVertex[index], origVertex[index + 1], origVertex[index + 2] });
		} else {
			return (null);
		}
	}

	/**
	 * Set the graph line data
	 * 
	 * @param vertex
	 * @param vertexCount
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 */
	public void setData(float[] vertex, int vertexCount, float xMin, float xMax, float yMin, float yMax, float[] origVertex) {
		this.origVertex = origVertex;
		if ((xMax - xMin) == 0) {
			xMax += 0.0001;
		}
		if ((yMax - yMin) == 0) {
			yMax += 0.0001;
		}
		this.vertex = vertex;
		this.vertexCount = vertexCount;
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
		axes.setRange(xMin, xMax, yMin, yMax);
		setData();
		pickX[0] = 0;
		pickX[1] = 0;
		valueX = -1;
	}

	private void setData() {
		buildLine(vertex, vertexCount);
		pickY[0] = height - axes.bottomMargin;
		pickY[1] = height - (axes.getHeight() + axes.bottomMargin);
	}

	/**
	 * The view was resized
	 * 
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		axes.resize(width, height);
		setData();
		pickX[0] = (int) ((valueX - axes.xMin) * axes.getXScale()) + axes.leftMargin;
		pickX[1] = pickX[0];
	}

	protected void createLine(int n) {
		lineX = new int[n];
		lineY = new int[n];
		lineX[0] = axes.leftMargin;
		lineX[1] = axes.leftMargin;
		lineY[0] = height - axes.bottomMargin;
		lineY[1] = height - axes.bottomMargin;
	}

	protected void createPick() {
		pickX = new int[2];
		pickY = new int[2];
		pickX[0] = 0;
		pickX[1] = 0;
		pickY[0] = height - axes.bottomMargin;
		pickY[1] = height - (axes.getHeight() + axes.bottomMargin);
	}

	protected void buildLine(float[] vertex, int n) {
		// increase buffer size if necessary
		if (n > length) {
			lineX = new int[n];
			lineY = new int[n];
			length = n;
		}
		// fill buffers
		numPoints = n;
		for (int i = 0; i < n; ++i) {
			int ii = i * 2;
			lineX[i] = (int) ((vertex[ii] - axes.xMin) * axes.xScale) + axes.leftMargin;
			lineY[i] = height - (int) ((vertex[ii + 1] - axes.yMin) * axes.yScale + axes.bottomMargin);
		}
	}
	
	public void setAxesEqualScale(boolean equalScale) {
		axes.equalScale = equalScale;
		axes.setRange(xMin, xMax, yMin, yMax);
		setData();
	}
	
	public boolean isAxesEqualScale() {
		return(axes.equalScale);
	}
}
