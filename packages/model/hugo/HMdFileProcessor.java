package model.hugo;

import model.utility.TomlParser;
import model.utility.FileHandler;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.nio.charset.*;
import java.nio.file.*;

public class HMdFileProcessor{
    private String frontMatter,postContent;
    private String filePath;
    private File mdFile;
    private boolean validMd;
    
    public HMdFileProcessor(String filePath){
        this.filePath = filePath;
        setupFile(this.filePath);
    }
    
    private void setupFile(String filepath){
        this.filePath = filePath;
        //System.out.println(filePath);
        this.mdFile = new File(filePath);
    }
    
    public String getFrontMatter(){
        return frontMatter;
    }

    public String getPostContent(){
        return postContent;
    }
    
    public void setFrontMatter(String frontMatter){
        this.frontMatter = frontMatter.trim() + "\n";
    }

    public void setPostContent(String postContent){
        this.postContent = postContent.trim() + "\n";
    }
    
    public boolean readHMdFile(){

        try{
            BufferedReader br = new BufferedReader(
            new InputStreamReader(
                        new FileInputStream(mdFile), StandardCharsets.UTF_8));
                    
            String line = br.readLine();
            for(int i=0; i < 3; i++){
                
                if(line.contains(TomlParser.TOML_IDENTIFIER)){
                    break;
                }
                
                //try first 3 lines
                if(i == 2){
                    validMd = false;
                    throw new Exception("No toml frontmatter found");
                    }
                    
                line = br.readLine();
            }
            
            frontMatter = postContent = "";
            for (line = br.readLine(); 
                    line != null && !line.contains(TomlParser.TOML_IDENTIFIER); 
                        line = br.readLine()) {
                    
                frontMatter += (line + "\n"); 
            }
            
            frontMatter = frontMatter.trim() + "\n";
            
            for(line = br.readLine(); 
                    line != null; 
                        line = br.readLine()){
                        
                postContent += (line + "\n"); 
            }
            
            postContent = postContent.trim() + "\n";
            
            validMd = true;
            return true;
            
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }


    }
    
    public boolean writeHMdFile(boolean force){
        String combinedMdText = TomlParser.TOML_IDENTIFIER + "\n"
            + frontMatter + TomlParser.TOML_IDENTIFIER + "\n \n"
            + postContent; 
            
            try{
                FileHandler.writeFile(combinedMdText,mdFile);
                return true;
                }
                catch(IOException e){
                    e.printStackTrace();
                    return false;
                }
    }
    
    public boolean writeHMdFile(){
    
        try{
            if(validMd){
                return writeHMdFile(true);
            }
            else{
            throw new Exception("Not valid HMd File.");
            }
        }
        catch(Exception e) {
            e.getMessage();
        }
        
        return false;
    }
    
    

}