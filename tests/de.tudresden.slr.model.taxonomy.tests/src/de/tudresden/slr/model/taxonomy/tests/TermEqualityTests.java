package de.tudresden.slr.model.taxonomy.tests;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

import de.tudresden.slr.model.taxonomy.Model;
import de.tudresden.slr.model.taxonomy.TaxonomyInjectorProvider;
import de.tudresden.slr.model.taxonomy.TaxonomyPackage;
import de.tudresden.slr.model.taxonomy.Term;
import de.tudresden.slr.model.taxonomy.util.TermUtils;

@RunWith(XtextRunner.class)
@InjectWith(TaxonomyInjectorProvider.class)
public class TermEqualityTests {
	@Inject
	ParseHelper<Model> parseHelper;
	
	//Taxonomy a and b are structural identical, but different objects
	/*
		Dimension 1 {
        	Term 1 {
        		Subterm 1,
        		Subterm 2
        	},
        	Term 2,
        	Subterm 1
		}
        Dimension 2 {
        	Term 1 {
        		Subterm 1,
        		Subterm 2
        	},
        	Term 2,
        	Term 3
        }
	 */
	private Model taxonomyA;
	private Model taxonomyB;

    @Before
    public void setUp() {
    	TaxonomyPackage.eINSTANCE.eClass();
    	
    	StringConcatenation taxonomyABuilder = new StringConcatenation();
		taxonomyABuilder.append("Dimension 1 {");
		taxonomyABuilder.newLine();
		taxonomyABuilder.append("Term 1 {");
		taxonomyABuilder.newLine();
		taxonomyABuilder.append("Subterm 1,");
		taxonomyABuilder.newLine();
		taxonomyABuilder.append("Subterm 2");
		taxonomyABuilder.newLine();
		taxonomyABuilder.append("},");
		taxonomyABuilder.newLine();
		taxonomyABuilder.append("Term 2,");
		taxonomyABuilder.newLine();
		taxonomyABuilder.append("Subterm 1");
		taxonomyABuilder.newLine();
		taxonomyABuilder.append("}");
		taxonomyABuilder.newLine();
		taxonomyABuilder.append("Dimension 2 {");
		taxonomyABuilder.newLine();			
		taxonomyABuilder.append("Term 1 {");
		taxonomyABuilder.newLine();			
		taxonomyABuilder.append("Subterm 1,");
		taxonomyABuilder.newLine();			
		taxonomyABuilder.append("Subterm 2");
		taxonomyABuilder.newLine();			
		taxonomyABuilder.append("},");
		taxonomyABuilder.newLine();			
		taxonomyABuilder.append("Term 2,");
		taxonomyABuilder.newLine();			
		taxonomyABuilder.append("Term 3");
		taxonomyABuilder.newLine();			
		taxonomyABuilder.append("}");
		
		try {
			taxonomyA = this.parseHelper.parse(taxonomyABuilder);
			taxonomyB = this.parseHelper.parse(taxonomyABuilder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw Exceptions.sneakyThrow(e);
		}
    }

	@Test
	public void nonHierarchicalTermsArentEqual() {
		//"Dimension 1" and "Dimension 2" aren't equal 
		Term dimension1a = taxonomyA.getDimensions().get(0);
		Term dimension2b = taxonomyB.getDimensions().get(1);
		Assert.assertFalse(TermUtils.equals(dimension1a, dimension2b));	
	}
	
	@Test
	public void nonHierarchicalTermsAreEqual() {
		//"Dimension 2" and "Dimension 2" are equal
		Term dimension2a = taxonomyA.getDimensions().get(1);
		Term dimension2b = taxonomyB.getDimensions().get(1);
		Assert.assertTrue(TermUtils.equals(dimension2a, dimension2b));	
	}
	
	@Test
	public void subTermsWithSameNameButDifferentParentsArentEqual(){
		//Dimension 1/Term 1/Subterm 1 and Dimension 2/Term 1/Subterm 1 aren't equal
		Term dimension1Term1Subterm1 = taxonomyA.getDimensions().get(0).getSubclasses().get(0).getSubclasses().get(0);
		Term dimension2Term1Subterm1 = taxonomyB.getDimensions().get(1).getSubclasses().get(0).getSubclasses().get(0);
		Assert.assertFalse(TermUtils.equals(dimension1Term1Subterm1, dimension2Term1Subterm1));	
	}
	
	@Test
	public void subTermsAreEqual(){
		//Dimension 1/Term 1/Subterm 1 and Dimension 1/Term 1/Subterm 1 are equal
		Term dimension1Term1Subterm1A = taxonomyA.getDimensions().get(0).getSubclasses().get(0).getSubclasses().get(0);
		Term dimension1Term1Subterm1B = taxonomyB.getDimensions().get(0).getSubclasses().get(0).getSubclasses().get(0);
		Assert.assertTrue(TermUtils.equals(dimension1Term1Subterm1A, dimension1Term1Subterm1B));	
	}
	
	@Test
	public void termsWithMatchingNamesFromDifferentLevelsArentEqual(){
		//Dimension 1/Subterm 1 and Dimension 1/Term 1/Subterm 1 aren't equal
		Term dimension1Subterm1 = taxonomyA.getDimensions().get(0).getSubclasses().get(2);
		Term dimension1Term1Subterm1 = taxonomyB.getDimensions().get(0).getSubclasses().get(0).getSubclasses().get(0);
		Assert.assertFalse(TermUtils.equals(dimension1Subterm1, dimension1Term1Subterm1));	
	}
}
