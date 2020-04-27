package de.tudresden.slr.questionnaire;

import org.junit.Test;
import org.mockito.Mockito;

public class HtmlSummaryTest {
	@Test
	public void testGenerateSummary() {
		Questionnaire questionnaire = Mockito.mock(Questionnaire.class);
		Mockito.when(questionnaire.getName()).thenReturn("questionnaireName");
		HtmlSummary summary = new HtmlSummary(questionnaire);
		
		summary = Mockito.spy(summary);
		summary.generateSummary();
		
		// verify, that body and header are generated and called once
		Mockito.verify(summary).generateBody();
		Mockito.verify(summary).generateHead();
	}

}
