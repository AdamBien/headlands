package com.airhacks.headlands.engine.control;

import com.airhacks.headlands.processors.boundary.EntryProcessorExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author airhacks.com
 */
public class NashornEngine {

    private Invocable invocable;
    private ScriptEngine scriptEngine;

    @PostConstruct
    public void initializeEngine() {
        ScriptEngineManager manager = new ScriptEngineManager();
        this.scriptEngine = manager.getEngineByName("javascript");
        this.invocable = (Invocable) scriptEngine;
    }

    public <T> T evalScript(String script, Class<T> interfaze) {
        try {
            this.scriptEngine.eval(script);
        } catch (ScriptException ex) {
            Logger.getLogger(EntryProcessorExecutor.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Cannot interpret: " + script
                    + " Problem in line: " + ex.getLineNumber()
                    + " Column: " + ex.getColumnNumber(), ex);
        }
        return instantiate(interfaze);
    }

    <T> T instantiate(Class<T> clazz) {
        return invocable.getInterface(clazz);
    }
}
