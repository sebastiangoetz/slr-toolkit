/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package de.tudresden.slr.ui.chart.logic;

import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.model.Chart;

public class ChartGenerator {

	public final static Chart createCiteBar(Map<String, Integer> input) {
		return new BarChartGenerator().createBar(input, "Cites per year for selected Class");
	}

	public final static Chart createBubble(List<BubbleDataContainer> input) {

		return new BubbleChartGenerator().createBubble(input);
	}

}
