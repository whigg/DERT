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

package gov.nasa.arc.dert.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Write a comma separated value (CSV) formatted file.
 *
 */
public class CsvWriter {

	// The file to be written to.
	protected String filename;

	// The writer
	protected PrintWriter writer;

	// Titles for columns
	protected String columnNames;
	
	// The value delimiter
	protected String delimiter;

	/**
	 * Constructor
	 * 
	 * @param filename
	 * @param columnName
	 */
	public CsvWriter(String filename, String[] columnName) {
		this(filename, columnName, ",");
	}

	/**
	 * Constructor
	 * 
	 * @param filename
	 * @param columnName
	 * @param delimiter
	 */
	public CsvWriter(String filename, String[] columnName, String delimiter) {
		this.filename = filename;
		this.delimiter = delimiter;
		columnNames = columnName[0];
		for (int i = 1; i < columnName.length; ++i) {
			columnNames += delimiter + columnName[i];
		}
	}

	/**
	 * Open the file and write the column titles.
	 * 
	 * @throws IOException
	 */
	public void open() throws IOException {
		writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
		writer.println(columnNames);
	}

	/**
	 * Close the file
	 */
	public void close() {
		writer.flush();
		writer.close();
	}

	/**
	 * Flush all output to the file
	 */
	public void flush() {
		writer.flush();
	}

	/**
	 * Write a line of values to the file
	 * 
	 * @param column
	 */
	public void writeLine(String[] column) {
		String str = column[0];
		for (int i = 1; i < column.length; ++i) {
			str += delimiter + column[i];
		}
		writer.println(str);
	}

	/**
	 * Write a line of values to the file
	 * 
	 * @param column
	 */
	public void writeLine(double[] column) {
		String str = Double.toString(column[0]);
		for (int i = 1; i < column.length; ++i) {
			str += delimiter + column[i];
		}
		writer.println(str);
	}

	/**
	 * Write a line of values to the file
	 * 
	 * @param column
	 */
	public void writeLine(float[] column) {
		String str = Float.toString(column[0]);
		for (int i = 1; i < column.length; ++i) {
			str += delimiter + column[i];
		}
		writer.println(str);
	}

	/**
	 * Write a line of values to the file with a timestamp
	 * 
	 * @param column
	 */
	public void writeLine(long time, double[] column) {
		String str = Long.toString(time);
		for (int i = 0; i < column.length; ++i) {
			str += delimiter + column[i];
		}
		writer.println(str);
	}

	/**
	 * Write a line of values to the file with a timestamp
	 * 
	 * @param column
	 */
	public void writeLine(long time, float[] column) {
		String str = Long.toString(time);
		for (int i = 0; i < column.length; ++i) {
			str += delimiter + column[i];
		}
		writer.println(str);
	}

	/**
	 * Write a line of values to the file with a timestamp and a type string
	 * 
	 * @param column
	 */
	public void writeLine(long time, String type, float[] column) {
		String str = Long.toString(time);
		str += delimiter + type;
		for (int i = 0; i < column.length; ++i) {
			str += delimiter + column[i];
		}
		writer.println(str);
	}

}
