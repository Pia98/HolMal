package com.holmal.app.holmal.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SavingFile {
    String filename = "holmal";

    public void checkIfFileExists(Context context, String data){
        File file = context.getFileStreamPath(filename);
        if(file.exists()){

        }
        else openFile(context, data);
    }

    public void openFile(Context context, String data) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            writeIntoFile(fileOutputStream, data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeIntoFile(FileOutputStream fileOutputStream, String data){
        try {
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFromFile(Context context){
        String temp="";
        try {
            FileInputStream fileInputStream = context.openFileInput(filename);
            int c;
            //String temp="";
            while ((c = fileInputStream.read()) != -1){
                temp = temp + Character.toString((char) c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
