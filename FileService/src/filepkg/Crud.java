package filepkg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class Crud {
	public ArrayList<String> c=new ArrayList<String>();

	public String getFileOper(String filenm, String oper) throws Exception {
		 //  BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		    
		    if(oper.equals("list"))
		    {		
		    	String path = new File(".").getCanonicalPath();    	
		    	getFile(path);
		    	System.out.println("files "+ c);
		    }
		    else if(oper.equals("get"))
		    {
		    	List<String> i=Get(filenm);
		    	System.out.println(i);
		    }
		    else if(oper.equals("update"))
		    {
		    	ArrayList<String> content = new ArrayList<String>();
		    	content.add("this");
		    	content.add("is");
		    	content.add("update");
		    	Update(filenm,content);
		    }
		    else if(oper.equals("create"))
		    {
		    	System.out.println("enter file");
		    	Create(filenm);
		    }
		   return "file done";
	}
	
	

	public ArrayList<String> getFile(String dirPath) {
	    try
		{
			File folder = new File(dirPath);
			File[] listOfFiles = folder.listFiles();

			    for (int i = 0; i < listOfFiles.length; i++) {
			      if (listOfFiles[i].isFile()) {
			      	String str=listOfFiles[i].getName();
			        c.add(str);
			      } else if (listOfFiles[i].isDirectory()) {
			      	String str=listOfFiles[i].getCanonicalPath();
			      	getFile(str);
			      }
			}
			return c;
		}
		catch (Exception e)
			  {
			    System.err.format("Exception occurred trying to read '%s'.", dirPath);
			    e.printStackTrace();
			    return null;
			  }
	}
	    public List<String> Get (String name)
	    {
	    	List<String> records = new ArrayList<String>();
			  try
			  {
			    BufferedReader reader = new BufferedReader(new FileReader(name));
			    String line;
			    while ((line = reader.readLine()) != null)
			    {
			      records.add(line);
			    }
			    reader.close();
			    return records;
			  }
			  catch (Exception e)
			  {
			    System.err.format("Exception occurred trying to read '%s'.", name);
			    e.printStackTrace();
			    return null;
			  }
	    }
	    public  int Update (String name,ArrayList<String> content)
	    {
	    	try {
	    		FileChannel outChan = new FileOutputStream(name, true).getChannel();
			    outChan.truncate(0);
			    outChan.close();
	    		PrintWriter writer = new PrintWriter(name, "UTF-8");
	            for(String s : content)
	                 writer.println(s);
	            writer.close();   
	            outChan.close();
		          //System.out.println("File has been updated successfully");
	            return 1;
	            
	            
	    	} catch (IOException e) {
	    		System.out.println("Exception Occurred:");
		        e.printStackTrace();
		        return -1;
		  }
	    }
	    
	    public  int Create(String name)
	    {
	    	try {
		     File file = new File(name);
	             boolean fvar = file.createNewFile();
		     if (fvar){
		          System.out.println("File has been created successfully");
		     	return 1;
		     }
		     else{
		          System.out.println("File already present at the specified location");
		     	return -1;
		     }
	    	} 
	    	catch (IOException e) {
	    		System.out.println("Exception Occurred:");
		        e.printStackTrace();
		        return -1;
		} 	
	}
}