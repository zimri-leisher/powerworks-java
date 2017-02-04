package powerworks.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventManager {

    public static List<EventListener> listeners = new ArrayList<EventListener>();
    public static List<Class<? extends EventListener>> staticListeners = new ArrayList<Class<? extends EventListener>>();

    public static void registerEventListener(EventListener listener) {
	listeners.add(listener);
    }

    public static void removeEventListener(EventListener listener) {
	listeners.remove(listener);
    }

    public static void registerStaticEventListener(Class<? extends EventListener> listener) {
	staticListeners.add(listener);
    }

    public static void removeStaticEventListener(Class<? extends EventListener> listener) {
	staticListeners.remove(listener);
    }

    public static <E extends Event> void sendEvent(E e) {
	for (EventListener listener : listeners) {
	    Method[] methods = listener.getClass().getMethods();
	    for (Method m : methods) {
		if (m.isAnnotationPresent(EventHandler.class)) {
		    if (m.getParameterCount() != 1 || !containsClass(m.getParameterTypes(), e.getClass()))
			throw new IllegalArgumentException("EventHandler methods cannot have more than 1 parameter and the parameter must be a child of Event");
		    if (!m.getName().contains("handle"))
			System.err.println("EventHandler method " + m.getDeclaringClass() + "." + m.getName() + " does not follow standard naming convention");
		    try {
			m.invoke(listener, e);
		    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			e1.printStackTrace();
		    }
		}
	    }
	}
	for (Class<? extends EventListener> listener : staticListeners) {
	    Method[] methods = listener.getClass().getMethods();
	    for (Method m : methods) {
		if (m.isAnnotationPresent(EventHandler.class)) {
		    if (m.getParameterCount() != 1 || !containsClass(m.getParameterTypes(), e.getClass()))
			throw new IllegalArgumentException("EventHandler methods cannot have more than 1 parameter and the parameter must be a child of Event");
		    if (!m.getName().contains("handle"))
			System.err.println("EventHandler method " + m.getDeclaringClass() + "." + m.getName() + " does not follow standard naming convention");
		    try {
			m.invoke(listener, e);
		    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			e1.printStackTrace();
		    }
		}
	    }
	}
    }

    static boolean containsClass(Class<?>[] classes, Class<?> toCheck) {
	for (Class<?> c : classes) {
	    if (toCheck.isAssignableFrom(c)) {
		return true;
	    }
	}
	return false;
    }
}
