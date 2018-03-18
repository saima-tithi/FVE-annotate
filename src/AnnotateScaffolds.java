import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;

public class AnnotateScaffolds {
	private String outputDir = "";
	private String inputScaffolds = "";
	private String blastDbDir = "";
	private int numThreads = 16;
	
	private int getFastaForScaffolds() {
		String cmd = "";
		try {
			cmd = "samtools faidx " + inputScaffolds + "\n";
			
			FileWriter shellFileWriter = new FileWriter(outputDir + "/run.sh");
			shellFileWriter.write("#!/bin/bash\n");
			shellFileWriter.write(cmd);
			shellFileWriter.close();

			ProcessBuilder builder = new ProcessBuilder("sh", outputDir + "/run.sh");
			builder.redirectError(Redirect.appendTo(new File(outputDir + "/log.txt")));
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while (reader.readLine() != null) {
			}
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int scaffoldNum = 0;
		cmd = "";
		String str = "";
		String[] results;
		String fastaHeader = "";
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inputScaffolds + ".fai"));
			while ((str = br.readLine()) != null) 
			{
				str = str.trim();
				results = str.split("\t");
				fastaHeader = results[0].trim();
				
				try {
					cmd = "cd " + outputDir + "\n"
							+ "mkdir scaffold-" + scaffoldNum + "\n"
							+ "cd scaffold-" + scaffoldNum + "\n"
							+ "bash filterbyname.sh in=" + inputScaffolds
							+ " out=scaffold-" + scaffoldNum + ".fasta names=" + fastaHeader + " include=t\n"
							+ "samtools faidx scaffold-" + scaffoldNum + ".fasta\n";
					
					FileWriter shellFileWriter = new FileWriter(outputDir + "/run.sh");
					shellFileWriter.write("#!/bin/bash\n");
					shellFileWriter.write(cmd);
					shellFileWriter.close();
		
					ProcessBuilder builder = new ProcessBuilder("sh", outputDir + "/run.sh");
					builder.redirectError(Redirect.appendTo(new File(outputDir + "/log.txt")));
					Process process = builder.start();
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					while (reader.readLine() != null) {
					}
					process.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
				}
				scaffoldNum++;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scaffoldNum;
	}
	
	public int initialize(String outputDir, String inputScaffolds, String blastDbDir, int numThreads) {
		this.outputDir = outputDir;
		this.inputScaffolds = inputScaffolds;
		this.blastDbDir = blastDbDir;
		this.numThreads = numThreads;
		
		int scaffoldNum = getFastaForScaffolds();
		return scaffoldNum;
	}
	
	public void runProdigal(int scaffoldNum) {
		String cmd = "";
		for (int i = 0; i < scaffoldNum; i++) {
			cmd = "";
			try {
				cmd = "cd " + outputDir + "\n"
						+ "cd scaffold-" + i + "\n"
						+ "prodigal -i scaffold-" + i + ".fasta -o genes.coords.gbk -a "
						+ "scaffold-" + i + "-proteins.faa -p meta -c\n";
				
				FileWriter shellFileWriter = new FileWriter(outputDir + "/run.sh");
				shellFileWriter.write("#!/bin/bash\n");
				shellFileWriter.write(cmd);
				shellFileWriter.close();

				ProcessBuilder builder = new ProcessBuilder("sh", outputDir + "/run.sh");
				builder.redirectError(Redirect.appendTo(new File(outputDir + "/log.txt")));
				Process process = builder.start();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				while (reader.readLine() != null) {
				}
				process.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void runBlastp(int scaffoldNum) {
		String cmd = "";
		for (int i = 0; i < scaffoldNum; i++) {
			cmd = "";
			try {
				cmd = "cd " + outputDir + "\n"
						+ "cd scaffold-" + i + "\n"
						+ "blastp -query scaffold-" + i + "-proteins.faa"
						+ " -db " + blastDbDir + "/nr -evalue 1e-5 -max_target_seqs 3 -num_threads " + numThreads
						+ " -outfmt '7' -out blastp-output.txt\n";
				
				FileWriter shellFileWriter = new FileWriter(outputDir + "/run.sh");
				shellFileWriter.write("#!/bin/bash\n");
				shellFileWriter.write(cmd);
				shellFileWriter.close();

				ProcessBuilder builder = new ProcessBuilder("sh", outputDir + "/run.sh");
				builder.redirectError(Redirect.appendTo(new File(outputDir + "/log.txt")));
				Process process = builder.start();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				while (reader.readLine() != null) {
				}
				process.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
