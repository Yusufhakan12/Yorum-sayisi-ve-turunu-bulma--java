
/**
*
* @author yusuf hakan güneş & yusuf.gunes5@ogr.sakarya.edu.tr
* @since 23.04.2023
* <p>
*  1A
* </p>
*/

package pj1;
import java.io.IOException; 
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileWriter;
public class Program {
	
	public static void main(String[] args) 
	{	String dosyaYolu;
	//terminalde girdi almak icin kullanildi 
		if(args.length>0)
		{
			dosyaYolu=args[0];
			
		}
		else
		{
			System.out.println("Dosya yolu giriniz: ");
			
			Scanner scanner=new Scanner(System.in);
				 dosyaYolu= scanner.nextLine();
				 
			
		}
		try 
		{    
		
		boolean multipleLineControl=false;
		boolean newFlag=false;
		boolean javaDocFlag=false;
	
		
		
		
		String WriteRegex = "/\\*(?!\\*)[^*]*(?:\\*(?!/)[^*]*)*\\*/";
		String newRegexCount = "/\\*(?!\\*).*";
		String OnlyLineRegex = "//.*$";
		String classRegex = "\\bclass\\s+([A-Za-z]+[A-Za-z0-9_]*)\\b";
		
		FileWriter myWriterJavaDoc=new FileWriter("javadoc.txt");
		FileWriter myWriter=new FileWriter("coksatir.txt");
		FileWriter myWriterOnlyLine=new FileWriter("teksatir.txt");
		
		String icerik=new String(Files.readAllBytes(Paths.get(dosyaYolu)));
		
	    Pattern oruntu=Pattern.compile(WriteRegex,Pattern.DOTALL);
	    Matcher eslesme=oruntu.matcher(icerik);
	    	
	    Pattern classPattern=Pattern.compile(classRegex);
   	 	Matcher classMatcher=classPattern.matcher(icerik);
   	 	
   	 	//sinif ismini buluyor
   	 	while(classMatcher.find())
   	 	{
   	 		System.out.println("Sinif: "+classMatcher.group(1).trim());
   	 	}
   	 	

   	 			
	    int TekSatir=0;
	    int cokSatirli=0;
	    int javaDoc=0;
	    //Dosyadaki kodları satirlara böldüm
	    String[] satirlar = icerik.split("\n"); 
	    //Bu döngü ile dosya satır satır okunuyor
	    for (String satir : satirlar) {
	    	
	    	 Pattern multipleLine=Pattern.compile(newRegexCount);
	    	 Matcher multipleLineMatcher=multipleLine.matcher(satir);
	    	 
	    	 Pattern onlyLine=Pattern.compile(OnlyLineRegex);
	    	 Matcher onlyLineMatcher=onlyLine.matcher(satir);
	    	 

	    	 //Contains ifadesi ile fonksiyonlar tespit ediliyor
	    	if ((satir.contains("public")||satir.contains("private")||satir.contains("protected"))  && satir.contains("(") && satir.contains(")") && (satir.contains("{")||(satir.contains(" ")))) 
	    	{
	           newFlag=true;
	            String[] parcalanmisSatir = satir.trim().split("\\(");
	            String fonksiyonAdi = parcalanmisSatir[0].trim();
	            System.out.println("        Fonksiyon: " + fonksiyonAdi);
	         
	            
	        }
	    	  //fonksiyonun içinde olup olmadığımızı kontrol etmek amaçlı yapildi
	        else if(newFlag)
	        {	
	        	//Coklu satir kontrol ediliyor
	        	if(multipleLineMatcher.find())
	        	{
	        		
	        	  cokSatirli++;
	        	  
	        	}
	        	//javaDoc yorum satiri 
	        	if(satir.contains("/**"))
		    	{ 
	        		javaDoc++;
	        		multipleLineControl=true;
		    		
		    	}
	        	// Java Doc içinde herhangi bir yorum satiri varmı onu kontrol ediyoruz
		    	if(multipleLineControl)
		    	{
		    		myWriterJavaDoc.write("\n"+satir+"\n");
		    		if(satir.contains("//"))
		    		{
		    			TekSatir--;
		    		}
		    		if(satir.contains("*/"))
		    		{
		    			multipleLineControl=false;
		    			myWriterJavaDoc.write("\n\n");
		    		}
		    	}
		    	
		    	//Tek satir yorum kontrol ediliyor
		    		if(satir.contains("//"))
			    	{
		    			TekSatir++;
		    			//Burada yorummları daha düzgün bir şekilde yazdırmak için regex kullandım
		    			while(onlyLineMatcher.find())
		    			{
		    				String onlyline=onlyLineMatcher.group().trim();
		    				myWriterOnlyLine.write(onlyline+"\n\n");
		    			}
			    	}
		    		   
		    		  
		    		  //Fonksiyonun bittiğini gösteriyor 
		    		if(satir.contains("}"))
		    		{
		    			System.out.println("                 Tek satir yorum sayisi: "+TekSatir);
		    			System.out.println("                 Cok satir yorum sayisi: "+cokSatirli);
		    			System.out.println("                 javaDoc yorum sayisi  : "+javaDoc);
		    			System.out.println("------------------------------------------");
		    			TekSatir=0;
		    			cokSatirli=0;
		    			javaDoc=0;
		    			newFlag=false;
		    		}

		    		
	        }
	    	   //Fonksiyonun dışındaki yorum satirlari kontrol ediyor
	        else
    		{
    			if(satir.contains("/**"))
    			{	javaDocFlag=true;
    				javaDoc++;
    			}
    			if(javaDocFlag)
    			{	
    				myWriterJavaDoc.write("\n"+satir+"\n");
    				
    				
    			}
    			if(satir.contains("*/"))
    			{
    				javaDocFlag=false;
    				myWriterJavaDoc.write("\n\n");
    				
    			}
    		}
	        
	    }
	    
	    
	    // Cok satirli yorumlari dosyaya yazdiriyor
	    while(eslesme.find())
	    {
	    	String CokSatirFile=eslesme.group().trim();
	    	myWriter.write(CokSatirFile);
	    	myWriter.write("\n\n");
	    	
	    }	
	    
	    
	    
	    myWriter.close();
	    myWriterJavaDoc.close();
	    myWriterOnlyLine.close();
	    
	
		}
		
	
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

}
