package edu.isi.mrs.pos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.net.URI;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.ling.TaggedWord;


public class POSTagger {
	
	public static void main(String args[]){
		
		Options options = createCommandLineOptions();
		CommandLineParser clp =new BasicParser();
		CommandLine cl=null;
		String filePath=null;
		String outputPath=null;
		try{
			cl = clp.parse(options, args);
			if (cl == null || cl.getOptions().length == 0 || cl.hasOption("help")) {
	        	HelpFormatter hf = new HelpFormatter();
	        	hf.printHelp(POSTagger.class.getSimpleName(), options);
	        }
		
	
			filePath = (String) cl.getOptionValue("filepath");
			outputPath = filePath;
			if (cl.hasOption("outputpath")) {
				outputPath = (String) cl.getOptionValue("outputpath");
			}
			
			
			//parse the file
			File mrsAbstract = new File(new URI("file://"+filePath));
			
			BufferedReader brAbstract = new BufferedReader(new FileReader(mrsAbstract));
			String line;
			String grammar = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
		    String[] parserOptions = { "-maxLength", "80", "-retainTmpSubcategories" };
		    LexicalizedParser lp = LexicalizedParser.loadModel(grammar, parserOptions);
		    TreebankLanguagePack tlp = lp.getOp().langpack();
		    
		    File file = new File(new URI("file://"+ outputPath));
		    
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bwAbstract = new BufferedWriter(fw);
			boolean hasWrittenLine=false;
			
			while((line=brAbstract.readLine())!=null){
				
				Tokenizer<? extends HasWord> toke =	tlp.getTokenizerFactory().getTokenizer(new StringReader(line));
				List<? extends HasWord> sentence2 = toke.tokenize();
				
			    Tree parse = lp.parse(sentence2);
			    
			    for(TaggedWord tw: parse.taggedYield())
			    {
			    	//System.out.println("Token: " + tw.word() + "- Tag:" + tw.tag());
			    	if(tw.tag().equalsIgnoreCase("NN")||tw.tag().equalsIgnoreCase("NNS")||tw.tag().equalsIgnoreCase("NNP")||tw.tag().equalsIgnoreCase("NNPS"))
			    	bwAbstract.write(tw.word() + " ");
			    	hasWrittenLine = true;
			    }
			    if(hasWrittenLine){
			    	bwAbstract.write("\n");
			    	hasWrittenLine=false;
			    }
			}
			bwAbstract.close();
			brAbstract.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	private static Options createCommandLineOptions() {
		Options options = new Options();
				options.addOption(new Option("filepath", "filepath", true, "location of the input file directory"));
				options.addOption(new Option("outputpath", "outputpath", true, "location of output file directory"));
				options.addOption(new Option("help", "help", false, "print this message"));
		return options;
	}

}
