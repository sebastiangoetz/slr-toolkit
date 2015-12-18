package de.tudresden.slr.ui.chart.views;

/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

public class ChartPreview implements PaintListener, ControlListener {

	private transient Canvas preview = null;

	private Chart cm = null;

	private transient boolean bIsPainting = false;

	private boolean dataPresent = false;

	private String textToShow;

	private transient Image buffer;

	private static int X_OFFSET = 3;

	private static int Y_OFFSET = 3;

	public ChartPreview() {
		// Do nothing in the constructor
	}

	/**
	 * @param cm
	 *            Chart Model
	 */
	public void renderModel(Chart cm) {
		// if ( cm == null )
		// {
		// return;
		// }
		this.cm = cm;

		if (preview != null && !preview.isDisposed()) {
			updateBuffer();
			preview.redraw();
		}
	}

	/**
	 * Generate and render the chart model,the chart image is stored in a
	 * buffer. The buffer will be updated only when the chart model is changed.
	 */
	private void updateBuffer() {
		if (bIsPainting) {
			return;
		}
		Throwable paintError = null;

		Rectangle re = preview.getClientArea();
		final Rectangle adjustedRe = new Rectangle(0, 0, re.width, re.height);

		if (adjustedRe.width - 2 * X_OFFSET <= 0
				|| adjustedRe.height - 2 * Y_OFFSET <= 0) {
			if (buffer != null && !buffer.isDisposed()) {
				buffer.dispose();
				buffer = null;
			}
			return;
		}

		if (cm == null) {
			buffer = null;
		} else {
			bIsPainting = true;
			Image oldBuffer = null;
			if (buffer == null) {
				buffer = new Image(Display.getDefault(), adjustedRe);
			} else {
				Rectangle ore = buffer.getBounds();

				oldBuffer = buffer;

				if (!adjustedRe.equals(ore)) {
					buffer = new Image(Display.getDefault(), adjustedRe);
				}
			}

			GC gc = new GC(buffer);

			// fill default backgournd as white.
			gc.setForeground(Display.getDefault().getSystemColor(
					SWT.COLOR_WHITE));
			gc.fillRectangle(buffer.getBounds());

			final Bounds bo = BoundsImpl.create(X_OFFSET, Y_OFFSET,
					adjustedRe.width - 2 * X_OFFSET, adjustedRe.height - 2
							* Y_OFFSET);
			// final Bounds bo = BoundsImpl
			// .create(re.x, re.y, re.width, re.height);

			IDeviceRenderer deviceRenderer = null;
			try {
				deviceRenderer = PluginSettings.instance().getDevice("dv.SWT"); //$NON-NLS-1$
				deviceRenderer
						.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, gc);
				bo.scale(72d / deviceRenderer.getDisplayServer()
						.getDpiResolution()); // CONVERT
				// TO
				// POINTS

				// GENERATE AND RENDER THE CHART
				final Generator gr = Generator.instance();
				RunTimeContext rtc = new RunTimeContext();

				GeneratedChartState state = gr.build(
						deviceRenderer.getDisplayServer(), cm, bo, null, rtc,
						null);

				gr.render(deviceRenderer, state);
			} catch (Exception ex) {
				paintError = ex;
			} finally {
				gc.dispose();
				if (deviceRenderer != null) {
					deviceRenderer.dispose();
				}
			}

			if (paintError != null) {
				buffer = oldBuffer;
			}
			if (oldBuffer != null && oldBuffer != buffer) {
				oldBuffer.dispose();
			}
			bIsPainting = false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events
	 * .PaintEvent)
	 */
	public void paintControl(PaintEvent pe) {
		GC gc = pe.gc;
		if (dataPresent) {
			if (buffer != null) {
				gc.drawImage(buffer, 0, 0);
			}
		} else {
			// Get the width of the canvas
			int canvasWidth = preview.getSize().x;
			int canvasHeight = preview.getSize().y;

			// Use the method stringExtent(String) which returns a
			// org.eclipse.swt.graphics.Point
			// and access the x variable of the Point which contains the
			// width of the given String
			// when plotted with the current Font on the Canvas
			int textWidth = gc.stringExtent(textToShow).x;

			// Plot centred by subtracting half the width of the string from
			// the centre of the Canvas width
			gc.drawText(textToShow, canvasWidth / 2, canvasHeight / 2);
		}

	}

	/**
	 * Set the preview canvas.
	 * 
	 * @param paintCanvas
	 */
	public void setPreview(Canvas paintCanvas) {
		this.preview = paintCanvas;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events
	 * .ControlEvent)
	 */
	public void controlMoved(ControlEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt
	 * .events.ControlEvent)
	 */
	public void controlResized(ControlEvent e) {
		if (preview != null && !preview.isDisposed()) {
			updateBuffer();
			preview.redraw();
		}
	}

	/**
	 * @param dataPresent
	 *            the dataPresent to set
	 */
	public void setDataPresent(boolean dataPresent) {
		this.dataPresent = dataPresent;
	}

	/**
	 * @return the textToShow
	 */
	public String getTextToShow() {
		return textToShow;
	}

	/**
	 * @param textToShow
	 *            the textToShow to set
	 */
	public void setTextToShow(String textToShow) {
		this.textToShow = textToShow;
	}
}

