package de.tudresden.slr.ui.chart.settings.parts;

import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.swt.graphics.RGB;

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
		
		private RGB legendBackgroundRGB = new RGB(155,155,155);
		//Shadow
		private RGB legendShadowRGB = new RGB(155,155,155);
		//Insets
		private double legendInsetTop = 000;
		private double legendInsetLeft = 0;
		private double legendInsetBottom = 0;
		private double legendInsetRight = 0;
		//Outline
		private boolean legendShowOutline = true;
		private LineStyle legendOutlineStyle = LineStyle.SOLID_LITERAL;
		private int legendOutlineThickness = 3;
		private RGB legendOutlineRGB = new RGB(0,0,0);

		//Misc
		private String legendTitle = "Test";
		//private Direction legendDirection = Direction.TOP_BOTTOM_LITERAL;
		private LegendItemType legendItemType = LegendItemType.CATEGORIES_LITERAL; //Andere Einstellung ist Abfuck? :D
		private double LegendMaxPercent = 0.3333; //Default 0,3333
		private Position LegendPosition = Position.RIGHT_LITERAL;
		//Separator
		private boolean legendShowSeparator = false;
		private LineStyle legendSeparatorStyle = LineStyle.SOLID_LITERAL;
		private int legendSeparatorThickness = 5;
		private RGB legendSeparatorRGB = new RGB(0,0,0);
		//Text
		private HorizontalAlignment legendHorizontalAlignment = HorizontalAlignment.CENTER_LITERAL;
		private VerticalAlignment legendVerticalAlignment = VerticalAlignment.TOP_LITERAL;
		private double legendTextRotation = 0;
		private float legendTextSize = 10;
		private boolean legendTextBold = false;
		private boolean legendTextItalic = false;
		private boolean legendTextStrikeThrough = false;
		private boolean legendTextUnderline = false;
		private boolean legendWordWrap = true;
		private double legendTitlePercent = 0.6; //Default
		private Position legendTitlePosition = Position.ABOVE_LITERAL;
		private double legendWrappingSize = 200;
		
		
		
		

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
			if(LegendPosition==Position.BELOW_LITERAL || LegendPosition==Position.ABOVE_LITERAL)
				return Orientation.HORIZONTAL_LITERAL;
			
			return Orientation.VERTICAL_LITERAL;
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

		public RGB getLegendBackgroundRGB() {
			return legendBackgroundRGB;
		}

		public void setLegendBackgroundRGB(RGB legendBackgroundRGB) {
			this.legendBackgroundRGB = legendBackgroundRGB;
		}

		public RGB getLegendShadowRGB() {
			return legendShadowRGB;
		}

		public void setLegendShadowRGB(RGB legendShadowRGB) {
			this.legendShadowRGB = legendShadowRGB;
		}

		public RGB getLegendOutlineRGB() {
			return legendOutlineRGB;
		}

		public void setLegendOutlineRGB(RGB legendOutlineRGB) {
			this.legendOutlineRGB = legendOutlineRGB;
		}

		public RGB getLegendSeparatorRGB() {
			return legendSeparatorRGB;
		}

		public void setLegendSeparatorRGB(RGB legendSeparatorRGB) {
			this.legendSeparatorRGB = legendSeparatorRGB;
		}

		public String getLegendTitle() {
			return legendTitle;
		}

		public void setLegendTitle(String legendTitle) {
			this.legendTitle = legendTitle;
		}

		/*public Direction getLegendDirection() {
			return legendDirection;
		}

		public void setLegendDirection(Direction legendDirection) {
			this.legendDirection = legendDirection;
		}
		*/
		
		
}

