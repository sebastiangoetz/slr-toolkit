package de.tudresden.slr.googlescholar;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Test;

import de.tudresden.slr.googlescholar.preferences.PreferenceConstants;

/**
 * IMPORTANT: due to this class being interwoven with eclipse rcp internals, it
 * can't be executed as a regular JUnit testcase
 */
public class GSWorkerTest {

	/*
	 * Helper class for simulating a output stream to dev/null
	 */
	public static class NullOutputStream extends OutputStream {
		@Override
		public void write(int b) throws IOException {
			// do nothing
		}

	}

	GSWorker gsWorker;

	@Before
	public void setUp() throws Exception {
		GSWorker.store.setValue(PreferenceConstants.P_MIN_WAIT, 1);
		GSWorker.store.setValue(PreferenceConstants.P_MIN_WAIT, 3);
		gsWorker = new GSWorker(new PrintWriter(new NullOutputStream()), null, null, null, null, null, null, null, null,
				null);
	}

	@Test
	public void testInitSessionDoesNotReturnNull() {
		assertNotNull(gsWorker.initSession());
	}

	@Test
	public void testWaitLikeUserDoesNotReturnNull() {
		assertNotNull(gsWorker.waitLikeUser());
	}

}
