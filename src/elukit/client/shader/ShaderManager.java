package elukit.client.shader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderManager {
	public static Shader defaultShader = null;
	
	public static void init(){
		int vertexShader = createShader("assets/shaders/default.vs",GL20.GL_VERTEX_SHADER);
		int fragmentShader = createShader("assets/shaders/default.fs",GL20.GL_FRAGMENT_SHADER);
		int def = GL20.glCreateProgram();
		GL20.glAttachShader(def, vertexShader);
		GL20.glAttachShader(def, fragmentShader);
		GL20.glLinkProgram(def);
		GL20.glUseProgram(def);
		defaultShader = new Shader(def);
	}
	
	public static int createShader(String filename, int shaderType) {
        int shader = GL20.glCreateShader(shaderType);
         
        if(shader == 0)
            return 0;
        try {
			GL20.glShaderSource(shader, readFileAsString(filename));
		} catch (Exception e) {
			e.printStackTrace();
		}
        GL20.glCompileShader(shader);
         
        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
            throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
         
        return shader;
    }
     
    public static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }
     
    public static String readFileAsString(String filename) throws Exception {
        StringBuilder source = new StringBuilder();
         
        FileInputStream in = new FileInputStream(filename);
         
        Exception exception = null;
         
        BufferedReader reader;
        try{
            reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
             
            Exception innerExc= null;
            try {
                String line;
                while((line = reader.readLine()) != null)
                    source.append(line).append('\n');
            }
            catch(Exception exc) {
                exception = exc;
            }
            finally {
                try {
                    reader.close();
                }
                catch(Exception exc) {
                    if(innerExc == null)
                        innerExc = exc;
                    else
                        exc.printStackTrace();
                }
            }
             
            if(innerExc != null)
                throw innerExc;
        }
        catch(Exception exc) {
            exception = exc;
        }
        finally {
            try {
                in.close();
            }
            catch(Exception exc) {
                if(exception == null)
                    exception = exc;
                else
                    exc.printStackTrace();
            }
             
            if(exception != null)
                throw exception;
        }
         
        return source.toString();
    }
}
