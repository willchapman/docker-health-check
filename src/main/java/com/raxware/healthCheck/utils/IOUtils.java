package com.raxware.healthCheck.utils;

import java.io.*;
import java.net.Socket;

public class IOUtils {
    private IOUtils() {}

    public static void quietClose(Writer writer) {
        try {
            if(writer != null)
                writer.close();
        }catch (Exception ignored) {}
    }

    public static void quietClose(Reader reader) {
        try {
            if(reader != null)
                reader.close();
        } catch (IOException ignored) {}
    }

    public static void quietClose(OutputStream outputStream) {
        try {
            if(outputStream != null)
                outputStream.close();
        }catch (Exception ignored) {}
    }

    public static void quietClose(InputStream inputStream) {
        try {
            if(inputStream != null)
                inputStream.close();
        }catch (Exception ignored) {}
    }

    public static void quietClose(Socket socket) {
        try {
            if(socket != null)
                socket.close();
        }catch (Exception ignored) {}
    }
}
