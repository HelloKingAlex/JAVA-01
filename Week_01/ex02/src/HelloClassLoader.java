import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader {
    public static void main(String[] args) {
        try {
            Object o = new HelloClassLoader().findClass("Hello").getDeclaredConstructor().newInstance();
            Class cls = o.getClass();
            Method m = cls.getDeclaredMethod("hello");
            m.invoke(o);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] bytes = getByteCodeFromFile("../resource/Hello.xlass");
            for (int i = 0; i< bytes.length;i++) {
                bytes[i] = (byte) (0xFF - bytes[i]);
            }
            return defineClass(name,bytes,0,bytes.length);
        } catch(Exception e) {
            throw new ClassNotFoundException("Class:" + name + "not found");
        }
    }
    private byte[] getByteCodeFromFile(String path) throws IOException{
        File file = new File(path);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
            && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        fi.close();
        return buffer;
    }
}
