package io.cucumber.skeleton;

import io.cucumber.java.Scenario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class ScenarioContext {
    private HashMap<Variable, Object> values = new HashMap<>();

    @Setter
    @Getter
    Scenario scenario = null;


    public <T> void put(Variable var, T object) {
        values.put(var, object);
    }

    public <T> T get(Variable var, Class<T> t) {
        Object obj = new Object();
        try {
            obj = values.get(var);
            return t.cast(obj);
        } catch (ClassCastException cce) {
            throw new RuntimeException(String.format("Variable '%s' is of type '%s'. '%s' expected",
                    var, obj.getClass(), t));
        }
    }

    public <T> List<T> getByClass(Class<T> t) {
        LinkedList<T> list = new LinkedList<>();
        values.values().stream().forEach(
                (o) -> {
                    try {
                        list.add(t.cast(o));
                    } catch (ClassCastException cce) {
                        return;
                    }
                }
        );

        return list;
    }


    @AllArgsConstructor
    public static class Variable {
        @NonNull
        protected String key;

        @Override
        public String toString() {
            return key;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Variable) {
                return key.equals(((Variable) obj).key);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }
}
