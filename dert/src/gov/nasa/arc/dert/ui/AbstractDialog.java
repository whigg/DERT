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

package gov.nasa.arc.dert.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Provides an abstract base class for dialogs.
 *
 */
public abstract class AbstractDialog extends JDialog {

	// Returned result (if any)
	protected Object result;

	// The area where extending classes can put widgets
	protected JPanel contentArea;

	// Panel for buttons at bottom of dialog
	protected JPanel buttonsPanel;

	// Message field at the top of the dialog
	protected JTextField messageText;

	// Default buttons
	protected JButton okButton, cancelButton;

	// Boolean argument and flag to add message text
	protected boolean boolArg, addMessage;

	// Dimensions
	protected int width, height;

	/**
	 * Constructor
	 * 
	 * @param parent
	 * @param title
	 * @param modal
	 * @param addMessage
	 */
	public AbstractDialog(Frame parent, String title, boolean modal, boolean addMessage) {
		this(parent, title, modal, false, addMessage);
	}

	/**
	 * Constructor
	 * 
	 * @param parent
	 * @param title
	 * @param modal
	 * @param boolArg
	 * @param addMessage
	 */
	public AbstractDialog(Frame parent, String title, boolean modal, boolean boolArg, boolean addMessage) {
		super(parent, title, modal);
		setLocationRelativeTo(parent);
		this.boolArg = boolArg;
		this.addMessage = addMessage;
	}

	/**
	 * Constructor
	 * 
	 * @param parent
	 * @param title
	 * @param modal
	 * @param addMessage
	 */
	public AbstractDialog(Dialog parent, String title, boolean modal, boolean addMessage) {
		this(parent, title, modal, false, addMessage);
	}

	/**
	 * Constructor
	 * 
	 * @param parent
	 * @param title
	 * @param modal
	 * @param closeOnly
	 * @param addMessage
	 */
	public AbstractDialog(Dialog parent, String title, boolean modal, boolean boolArg, boolean addMessage) {
		super(parent, title, modal);
		setLocationRelativeTo(parent);
		this.boolArg = boolArg;
		this.addMessage = addMessage;
	}

	protected void build() {
		getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		getRootPane().setLayout(new BorderLayout());
		if (addMessage) {
			messageText = new JTextField();
			messageText.setEditable(false);
			messageText.setBackground(getBackground());
			messageText.setForeground(Color.blue);
			messageText.setBorder(null);
			getRootPane().add(messageText, BorderLayout.NORTH);
		}
		contentArea = new JPanel();
		getRootPane().add(contentArea, BorderLayout.CENTER);
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		if (boolArg) {
			okButton = new JButton("Close");
//			okButton.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent event) {
//					if (okPressed()) {
//						close();
//					}
//				}
//			});
//			buttonsPanel.add(okButton);
		} else {
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					if (cancelPressed()) {
						close();
					}
				}
			});
			buttonsPanel.add(cancelButton);

			okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					if (okPressed()) {
						close();
					}
				}
			});
			buttonsPanel.add(okButton);
		}
		getRootPane().add(buttonsPanel, BorderLayout.SOUTH);
		if (okButton != null) {
			getRootPane().setDefaultButton(okButton);
		} else if (cancelButton != null) {
			getRootPane().setDefaultButton(cancelButton);
		}
	}

	protected abstract boolean okPressed();

	protected boolean cancelPressed() {
		result = null;
		return (true);
	}

	/**
	 * Open the dialog
	 */
	public Object open() {
		result = null;
		if (contentArea == null) {
			build();
			if ((width == 0) || (height == 0)) {
				pack();
			} else {
				setSize(width, height);
			}
		}
		setVisible(true);
		return(result);
	}

	/**
	 * Close the dialog
	 */
	public void close() {
		setVisible(false);
	}
}
