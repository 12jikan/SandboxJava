package com.onyx.window;

import org.lwjgl.*;
// import org.lwjgl.glfw.*;
// import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.glfw.GLFWErrorCallback;

// import java.io.IOException;
import java.nio.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private int Width, Height;
    private String Title;

    // The window handle
    private long Window = NULL;

    public Window(int width, int height, String title) {
        this.setWindowSize(width, height);
        this.setWindowTitle(title);
    };

    public Window() {
        this(1280, 720, "Default Title");
    };

    public void createWindow() {
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");
        init();
    };

    public void init() {
        // Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW:
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW...");

        // Configure GLFW:
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);

        // Create the window:
        setWindow(glfwCreateWindow(this.Width, this.Height, this.Title, NULL, NULL));
        if (this.Window == NULL)
            throw new IllegalStateException("Failed to create the GLFW window...");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(this.Window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
        

        // Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(this.Window, pWidth, pHeight);

		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(this.Window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(this.Window);
    };

    // Getters and setters for Window:
    // ----------------------------------------------------------------

    // Set window Width and Height
    private void setWindowSize(int width, int height) {
        this.Width = width;
        this.Height = height;
    };

    // Get window Width and Height
    // private int[] getWindowSize() {
    //     return new int[] {
    //             this.Width,
    //             this.Height
    //     };
    // };

    // Set the title
    public void setWindowTitle(String title) {
        this.Title = title;
    };

    // Access title
    public String getWindowTitle() {
        return this.Title;
    };

    // Set the window
    public void setWindow(long window) {
        this.Window = window;
    };

    // Get the current window
    public long getWindow() {
        return this.Window;
    };

    // finalize() for Window:
    // ----------------------------------------------------------------
    protected void finalize() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(this.Window);
        glfwDestroyWindow(this.Window);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        // Print exit message:
        System.out.println("Program Finished!");
    };
}