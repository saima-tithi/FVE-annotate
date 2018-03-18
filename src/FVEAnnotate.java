
public class FVEAnnotate {
	private static String outputDir = "";
	private static String inputScaffolds = "";
	private static String blastDbDir = "";
	private static int numThreads = 16;
	
	private static void printUsage() {
	    System.out.println("Usage:");
        System.out.println(
                "java -cp bin FVEAnnotate -in $inputScaffolds -blastDbDir $blastDatabase -o $outputDirectory");
        System.out.println("-in: full path of input scaffolds (in fasta format).");
        System.out.println("-blastDbDir: full path of directory containing blast non-redundant protein database.");
        System.out.println("-o: full path of output directory.");
	}
	
	private static void parseArguments(String[] args) {
		if (args.length == 0) {
			printUsage();
			System.exit(1);
		} else {
			for (int i = 0; i < args.length; i++) {
				if (args[i].startsWith("-")) {
					if ((i + 1) >= args.length) {
						System.out.println("Missing argument after " + args[i] + ".");
						printUsage();
						System.exit(1);
					} else {
						if (args[i].equals("-o")) {
							outputDir = args[i + 1];
						} else if (args[i].equals("-in")) {
							inputScaffolds = args[i + 1];
						} else if (args[i].equals("-blastDbDir")) {
							blastDbDir = args[i + 1];
						} else if (args[i].equals("-p")) {
							numThreads = Integer.parseInt(args[i + 1]);
						} else {
							System.out.println("Invalid argument.");
							printUsage();
							System.exit(1);
						}
					}
				}
			}
		} // finish parsing arguments
	}

	public static void main(String[] args) {
		parseArguments(args);
		System.out.println("Finished parsing command line arguments");
		
		AnnotateScaffolds annotateScaffolds = new AnnotateScaffolds();
		int scaffoldNum = annotateScaffolds.initialize(outputDir, inputScaffolds, blastDbDir, numThreads);
		System.out.println("Input contains " + scaffoldNum + " scaffolds.");
		
		System.out.println("Started running Prodigal for " + scaffoldNum + " scaffolds.");
		annotateScaffolds.runProdigal(scaffoldNum);
		System.out.println("Finished running Prodigal for " + scaffoldNum + " scaffolds.");
		
		System.out.println("Started running Blastp for " + scaffoldNum + " scaffolds.");
		annotateScaffolds.runBlastp(scaffoldNum);
		System.out.println("Finished running Blastp for " + scaffoldNum + " scaffolds.");
	}
}
