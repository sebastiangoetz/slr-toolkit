package de.tudresden.slr.ui.chart.logic;

import de.tudresden.slr.model.taxonomy.Term;

public class BubbleDataContainer {

	private Term xTerm;
	private Term yTerm;
	private int bubbleSize;

	//TODO: What's the purpose of BubbleDataContainer.hashCode()? Can it be removed and why does it call Term.hashCode()?
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bubbleSize;
		result = prime * result + ((xTerm == null) ? 0 : xTerm.hashCode());
		result = prime * result + ((yTerm == null) ? 0 : yTerm.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BubbleDataContainer) {
			BubbleDataContainer b = ((BubbleDataContainer)obj);
			if(b.getxTerm().equals(this.getxTerm()) &&
					b.getyTerm().equals(this.getyTerm()) &&
							b.getBubbleSize() == this.getBubbleSize())
				return true;					
		}
		return false;
	}

	public Term getxTerm() {
		return xTerm;
	}

	public void setxTerm(Term xTerm) {
		this.xTerm = xTerm;
	}

	public Term getyTerm() {
		return yTerm;
	}

	public void setyTerm(Term yTerm) {
		this.yTerm = yTerm;
	}

	public int getBubbleSize() {
		return bubbleSize;
	}

	public void setBubbleSize(int bubbleSize) {
		this.bubbleSize = bubbleSize;
	}

	public BubbleDataContainer(Term xTerm, Term yTerm, int bubbleSize) {
		super();
		this.xTerm = xTerm;
		this.yTerm = yTerm;
		this.bubbleSize = bubbleSize;
	}

}
