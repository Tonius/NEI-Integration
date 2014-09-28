package tonius.neiintegration;

import java.lang.reflect.Method;

public class Hacks {
    
    public static Class getClass(String name) {
        try {
            Class clazz = Class.forName(name);
            return clazz;
        } catch (Exception e) {
            NEIIntegration.log.error("Failed to find class " + name);
        }
        return null;
    }
    
    public static MethodInvoker getMethodInvoker(Class clazz, String name, Object instance, Class... parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(name, parameterTypes);
            return new MethodInvoker(method, instance);
        } catch (Exception e) {
            NEIIntegration.log.error("Failed to find method " + name + " in class " + clazz.getName());
        }
        return null;
    }
    
    public static class MethodInvoker {
        private Object obj;
        private Method method;
        
        public MethodInvoker(Method method, Object obj) {
            this.method = method;
            this.obj = obj;
        }
        
        public Object invoke(Object... args) {
            try {
                return this.method.invoke(this.obj, args);
            } catch (Exception e) {
                NEIIntegration.log.error("Failed to invoke method " + this.method.getName() + " from class " + this.method.getDeclaringClass().getName());
            }
            return null;
        }
    }
    
}
