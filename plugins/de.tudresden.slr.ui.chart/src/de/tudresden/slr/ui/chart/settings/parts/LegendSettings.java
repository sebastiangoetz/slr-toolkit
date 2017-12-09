package de.tudresden.slr.ui.chart.settings.parts;

import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.layout.ClientArea;

import de.tudresden.slr.ui.chart.settings.ChartConfiguration;

public class LegendSettings {

		
		private LegendSettings() {
		}
		
		public static LegendSettings get() {
			return LEGENDSETTINGS;
		}

		private static LegendSettings LEGENDSETTINGS= new LegendSettings();
		/*
		private ClientArea legendArea;
		private Direction legendDirection;
		private int legendEllipsis;
		private int legendHorizontalSpacing;
		private int legendVerticalSpacing;
		private LegendItemType legendItemType;
		private double legendMaxPercent;
		private Orientation legendOrientation;
		private Position legendPosition;
		private LineAttributes legendSeparator;
		private Text legendText;
		private Label legendTitle;
		private double legendTitleMaxPercent;
		private double legendWrappingSize;
		*/
		//Background
		private int legendBackgroundRed = 155;
		private int legendBackgroundGreen = 155;
		private int legendBackgroundBlue = 155;
		//Shadow
		private int legendShadowRed = 190;
		private int legendShadowGreen = 190;
		private int legendShadowBlue = 190;
		//Insets
		private double legendInsetTop = 000;
		private double legendInsetLeft = 0;
		private double legendInsetBottom = 0;
		private double legendInsetRight = 0;
		//Outline
		private boolean legendShowOutline = true;
		private LineStyle legendOutlineStyle = LineStyle.SOLID_LITERAL;
		private int legendOutlineThickness = 1;
		private int legendOutlineRed = 0;
		private int legendOutlineGreen = 0;
		private int legendOutlineBlue = 0;
		//Misc
		//private Direction legendDirection = Direction.TOP_BOTTOM_LITERAL;
		private LegendItemType legendItemType = LegendItemType.CATEGORIES_LITERAL; //Andere Einstellung ist Abfuck? :D
		private double LegendMaxPercent = 0.3333; //Default 0,3333
		private Orientation LegendOrientation = Orientation.VERTICAL_LITERAL;
		private Position LegendPosition = Position.RIGHT_LITERAL;
		//Separator
		private boolean legendShowSeparator = false;
		private LineStyle legendSeparatorStyle = LineStyle.SOLID_LITERAL;
		private int legendSeparatorThickness = 5;
		private int legendSeparatorRed = 0;
		private int legendSeparatorGreen = 0;
		private int legendSeparatorBlue = 0;
		//Text
		private HorizontalAlignment legendHorizontalAlignment = HorizontalAlignment.LEFT_LITERAL;
		private VerticalAlignment legendVerticalAlignment = VerticalAlignment.TOP_LITERAL;
		private double legendTextRotation = 0;
		private float legendTextSize = 12;
		private boolean legendTextBold = false;
		private boolean legendTextItalic = false;
		private boolean legendTextStrikeThrough = false;
		private boolean legendTextUnderline = false;
		private boolean legendWordWrap = true;
		private double legendTitlePercent = 0.6; //Default
		private Position legendTitlePosition = Position.ABOVE_LITERAL;
		private double legendWrappingSize = 30;
		
		
		
		
		public int getLegendBackgroundRed() {
			return legendBackgroundRed;
		}

		public void setLegendBackgroundRed(int legendBackgroundRed) {
			this.legendBackgroundRed = legendBackgroundRed;
		}

		public int getLegendBackgroundGreen() {
			return legendBackgroundGreen;
		}

		public void setLegendBackgroundGreen(int legendBackgroundGreen) {
			this.legendBackgroundGreen = legendBackgroundGreen;
		}

		public int getLegendBackgroundBlue() {
			return legendBackgroundBlue;
		}

		public void setLegendBackgroundBlue(int legendBackgroundBlue) {
			this.legendBackgroundBlue = legendBackgroundBlue;
		}

		public int getLegendShadowRed() {
			return legendShadowRed;
		}

		public void setLegendShadowRed(int legendShadowRed) {
			this.legendShadowRed = legendShadowRed;
		}

		public int getLegendShadowGreen() {
			return legendShadowGreen;
		}

		public void setLegendShadowGreen(int legendShadowGreen) {
			this.legendShadowGreen = legendShadowGreen;
		}

		public int getLegendShadowBlue() {
			return legendShadowBlue;
		}

		public void setLegendShadowBlue(int legendShadowBlue) {
			this.legendShadowBlue = legendShadowBlue;
		}

		public double getLegendInsetTop() {
			return legendInsetTop;
		}

		public void setLegendInsetTop(double legendInsetTop) {
			this.legendInsetTop = legendInsetTop;
		}

		public double getLegendInsetLeft() {
			return legendInsetLeft;
		}

		public void setLegendInsetLeft(double legendInsetLeft) {
			this.legendInsetLeft = legendInsetLeft;
		}

		public double getLegendInsetBottom() {
			return legendInsetBottom;
		}

		public void setLegendInsetBottom(double legendInsetBottom) {
			this.legendInsetBottom = legendInsetBottom;
		}

		public double getLegendInsetRight() {
			return legendInsetRight;
		}

		public void setLegendInsetRight(double legendInsetRight) {
			this.legendInsetRight = legendInsetRight;
		}

		public boolean isLegendShowOutline() {
			return legendShowOutline;
		}

		public void setLegendShowOutline(boolean legendShowOutline) {
			this.legendShowOutline = legendShowOutline;
		}

		public LineStyle getLegendOutlineStyle() {
			return legendOutlineStyle;
		}

		public void setLegendOutlineStyle(LineStyle legendOutlineStyle) {
			this.legendOutlineStyle = legendOutlineStyle;
		}

		public int getLegendOutlineThickness() {
			return legendOutlineThickness;
		}

		public void setLegendOutlineThickness(int legendOutlineThickness) {
			this.legendOutlineThickness = legendOutlineThickness;
		}

		public int getLegendOutlineRed() {
			return legendOutlineRed;
		}

		public void setLegendOutlineRed(int legendOutlineRed) {
			this.legendOutlineRed = legendOutlineRed;
		}

		public int getLegendOutlineGreen() {
			return legendOutlineGreen;
		}

		public void setLegendOutlineGreen(int legendOutlineGreen) {
			this.legendOutlineGreen = legendOutlineGreen;
		}

		public int getLegendOutlineBlue() {
			return legendOutlineBlue;
		}

		public void setLegendOutlineBlue(int legendOutlineBlue) {
			this.legendOutlineBlue = legendOutlineBlue;
		}

		public LegendItemType getLegendItemType() {
			return legendItemType;
		}

		public void setLegendItemType(LegendItemType legendItemType) {
			this.legendItemType = legendItemType;
		}

		public double getLegendMaxPercent() {
			return LegendMaxPercent;
		}

		public void setLegendMaxPercent(double legendMaxPercent) {
			LegendMaxPercent = legendMaxPercent;
		}

		public Orientation getLegendOrientation() {
			return LegendOrientation;
		}

		public void setLegendOrientation(Orientation legendOrientation) {
			LegendOrientation = legendOrientation;
		}

		public Position getLegendPosition() {
			return LegendPosition;
		}

		public void setLegendPosition(Position legendPosition) {
			LegendPosition = legendPosition;
		}

		public boolean isLegendShowSeparator() {
			return legendShowSeparator;
		}

		public void setLegendShowSeparator(boolean legendShowSeparator) {
			this.legendShowSeparator = legendShowSeparator;
		}

		public LineStyle getLegendSeparatorStyle() {
			return legendSeparatorStyle;
		}

		public void setLegendSeparatorStyle(LineStyle legendSeparatorStyle) {
			this.legendSeparatorStyle = legendSeparatorStyle;
		}

		public int getLegendSeparatorThickness() {
			return legendSeparatorThickness;
		}

		public void setLegendSeparatorThickness(int legendSeparatorThickness) {
			this.legendSeparatorThickness = legendSeparatorThickness;
		}

		public int getLegendSeparatorRed() {
			return legendSeparatorRed;
		}

		public void setLegendSeparatorRed(int legendSeparatorRed) {
			this.legendSeparatorRed = legendSeparatorRed;
		}

		public int getLegendSeparatorGreen() {
			return legendSeparatorGreen;
		}

		public void setLegendSeparatorGreen(int legendSeparatorGreen) {
			this.legendSeparatorGreen = legendSeparatorGreen;
		}

		public int getLegendSeparatorBlue() {
			return legendSeparatorBlue;
		}

		public void setLegendSeparatorBlue(int legendSeparatorBlue) {
			this.legendSeparatorBlue = legendSeparatorBlue;
		}

		public HorizontalAlignment getLegendHorizontalAlignment() {
			return legendHorizontalAlignment;
		}

		public void setLegendHorizontalAlignment(HorizontalAlignment legendHorizontalAlignment) {
			this.legendHorizontalAlignment = legendHorizontalAlignment;
		}

		public VerticalAlignment getLegendVerticalAlignment() {
			return legendVerticalAlignment;
		}

		public void setLegendVerticalAlignment(VerticalAlignment legendVerticalAlignment) {
			this.legendVerticalAlignment = legendVerticalAlignment;
		}

		public double getLegendTextRotation() {
			return legendTextRotation;
		}

		public void setLegendTextRotation(double legendTextRotation) {
			this.legendTextRotation = legendTextRotation;
		}

		public float getLegendTextSize() {
			return legendTextSize;
		}

		public void setLegendTextSize(float legendTextSize) {
			this.legendTextSize = legendTextSize;
		}

		public boolean isLegendTextBold() {
			return legendTextBold;
		}

		public void setLegendTextBold(boolean legendTextBold) {
			this.legendTextBold = legendTextBold;
		}

		public boolean isLegendTextItalic() {
			return legendTextItalic;
		}

		public void setLegendTextItalic(boolean legendTextItalic) {
			this.legendTextItalic = legendTextItalic;
		}

		public boolean isLegendTextStrikeThrough() {
			return legendTextStrikeThrough;
		}

		public void setLegendTextStrikeThrough(boolean legendTextStrikeThrough) {
			this.legendTextStrikeThrough = legendTextStrikeThrough;
		}

		public boolean isLegendTextUnderline() {
			return legendTextUnderline;
		}

		public void setLegendTextUnderline(boolean legendTextUnderline) {
			this.legendTextUnderline = legendTextUnderline;
		}

		public boolean isLegendWordWrap() {
			return legendWordWrap;
		}

		public void setLegendWordWrap(boolean legendWordWrap) {
			this.legendWordWrap = legendWordWrap;
		}

		public double getLegendTitlePercent() {
			return legendTitlePercent;
		}

		public void setLegendTitlePercent(double legendTitlePercent) {
			this.legendTitlePercent = legendTitlePercent;
		}

		public Position getLegendTitlePosition() {
			return legendTitlePosition;
		}

		public void setLegendTitlePosition(Position legendTitlePosition) {
			this.legendTitlePosition = legendTitlePosition;
		}

		public double getLegendWrappingSize() {
			return legendWrappingSize;
		}

		public void setLegendWrappingSize(double legendWrappingSize) {
			this.legendWrappingSize = legendWrappingSize;
		}

		/*public Direction getLegendDirection() {
			return legendDirection;
		}

		public void setLegendDirection(Direction legendDirection) {
			this.legendDirection = legendDirection;
		}
		*/
		
		
}

