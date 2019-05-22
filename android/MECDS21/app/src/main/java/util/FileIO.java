package util;


import java.io.BufferedReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by misiyu on 18-4-10.
 */

public class FileIO  {
    private String filename ;
    public FileIO(String filename,String fileDir){
        this.filename = fileDir+filename ;
    }
    public void save(String inputText){
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(filename);
            fileWriter.append(inputText);
            fileWriter.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(fileWriter != null) fileWriter.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public List<String> read(){
        List<String> content = new ArrayList<>();

        FileReader fileReader = null ;
        BufferedReader bufferedReader = null;
        try{
            fileReader = new FileReader(filename);
            bufferedReader= new BufferedReader(fileReader);
            String line = "";
            while((line = bufferedReader.readLine())!=null)
                content.add(line);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(bufferedReader!=null) bufferedReader.close();
                if(fileReader != null) fileReader.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return content;
    }
}
