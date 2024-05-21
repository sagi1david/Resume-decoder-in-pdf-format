package pdf;
import java.io.File;
import java.util.regex.*;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.bson.Document;

public class decoder 
{
	private static final String EMAIL_PATTERN = "[A-Za-z.0-9_-]+@+[A-Za-z]+.+([comCOM]{3}|[coCO]{2}+[.]{1}+[ilIL]{2})";
	private static final String ADDRESS_PATTERN = "[A-Za-z]+[,.\s]{2}+[ISRAELisrael]{6}";
	private static final String LINKEDIN_PATTERN = "[inIN]{2}+[/]{1}+[A-Za-z.0-9_-]*+[\r\n]{0,}+[A-Za-z.0-9_-]{0,}";
	private static final String PHONE_PATTERN = "[0-9]{3}+[-]+5+[0-9]{1}+[-]+[0-9]{7}|0+5+[0-9]{1}+[-]+[0-9]{7}|0+5+[0-9]{8}";
	private static final String ID_PATTERN = "[023]{1}+[0-9]{8}";

	public static void main(String[] args)
	
	{
		String email = null ,address = null ,linkedin = null ,phone = null ,id = null;
		String pdftextdata;

		try
		{
			PDDocument document = PDDocument.load(new File("C:\\CV - Sagi David.pdf"));

			PDFTextStripper pdftext = new PDFTextStripper();

			pdftextdata = pdftext.getText(document);

			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(pdftextdata);

			while(matcher.find())
			{
				email = matcher.group();
				System.out.println(email);
			}

			pattern = Pattern.compile(ADDRESS_PATTERN);
			matcher = pattern.matcher(pdftextdata);

			while(matcher.find())
			{
				address = matcher.group();
				System.out.println(address);
			}

			pattern = Pattern.compile(LINKEDIN_PATTERN);
			matcher = pattern.matcher(pdftextdata);

			while(matcher.find())
			{
				linkedin = matcher.group();
				linkedin = "www.linkedin.com/" + linkedin;
				System.out.println(linkedin);
			}
			 
			pattern = Pattern.compile(PHONE_PATTERN);
			matcher = pattern.matcher(pdftextdata);

			while(matcher.find())
			{
				phone = matcher.group();
				System.out.println(phone);
			}

			pattern = Pattern.compile(ID_PATTERN);
			matcher = pattern.matcher(pdftextdata);

			while(matcher.find())
			{
				String temp = matcher.group();

				if(!temp.equals(phone.substring(0, 9)))
					id = temp;

				System.out.println(id);
			}


			System.out.println(pdftextdata);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			// Creating a Mongo client 
			MongoClient mongoClient = new MongoClient( "localhost" , 27017 ); 
			System.out.println("Created Mongo Connection successfully"); 

			MongoDatabase db = mongoClient.getDatabase("resumes");
			System.out.println("Get database is successful");

			//creating collection or get collection if exists.
			MongoCollection<Document>  collection= db.getCollection("channels");
			System.out.println("collection created");

			//Inserting sample records by creating documents.
			Document doc =new Document("email",email);
			doc.append("address",address);  
			doc.append("linkedin",linkedin);  
			doc.append("phone", phone);
			doc.append("id", id);
			collection.insertOne(doc);
			System.out.println("Insert is completed");

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
