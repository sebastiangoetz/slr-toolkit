package de.tudresden.slr.metainformation.data;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class AuthorTest {

	@Test
	public void testEmptyEmailToString() {
		// when email is empty, toString should not include an email address
		Author author = new Author("name", "", "university of x");
		assertFalse(author.toString().contains("email"));
	}
	
	@Test
	public void testEmptyOrganisationToString() {
		// when organisation is empty, toString should not include an organisation
		Author author = new Author("name", "a@b.com", "");
		assertFalse(author.toString().contains("organisation"));
	}

}
