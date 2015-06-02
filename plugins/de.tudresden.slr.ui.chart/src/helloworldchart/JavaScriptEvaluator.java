package helloworldchart;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JavaScriptEvaluator {

	private ScriptEngineManager manager = null;
	private ScriptEngine engine = null;

	public ScriptEngine getEngine() {
		return engine;
	}

	public JavaScriptEvaluator() {
		manager = new ScriptEngineManager();
		engine = manager.getEngineByName("nashorn");

	}

	public void evaluate(String content) {

		try {
			engine.put("x", 100);
			engine.put("y", 100);
			engine.eval("var z = x + y;");
			int value = (Integer) engine.get("z");
			System.out.println(value);
		} catch (final ScriptException se) {
			System.err.println(se);
		}
	}

}
