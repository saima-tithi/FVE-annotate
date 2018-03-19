# FastViromeExplorer-annotate
Get functional annotation of proteins from scaffolds.

# Installation
FastViromeExplorer requires the following tools installed in the user's machine.
1. bbmap
2. Blast
3. Prodigal
 
## Download FastViromeExplorer-annotate
You can download FastViromeExplorer-annotate directly from github and extract it. You can also download it using the following command:
```bash
git clone https://github.com/saima-tithi/FVE-annotate.git
```
From now on, we will refer the FastViromeExplorer-annotate directory in the user's local machine as `project directory`. The `project directory` will contain 3 folders: src, bin, and tools-linux.

For installing the tool dependencies, add the following lines in the .bashrc. Please make sure to change the "path-to-project-directory" into the correct path of FastViromeExplorer-novel project directory.

```bash
#Install bbmap
export PATH=$PATH:/path-to-project-directory/tools-linux/bbmap
#Install Blast
export PATH=$PATH:/path-to-project-directory/tools-linux/ncbi-blast-2.7.1+/bin
#Install Prodigal
export PATH=$PATH:/path-to-project-directory/tools-linux/Prodigal
```

# Run FastViromeExplorer-annotate
Run FastViromeExplorer and FastViromeExplorer-novel to generate final-scaffolds.fasta file.

Download the blast non-redundant protein database using the following commands:
```bash
mkdir blast-db-directory
cd /path-to-blast-db-directory
wget 'ftp://ftp.ncbi.nlm.nih.gov/blast/db/nr.*.tar.gz'
cat nr.*.tar.gz | tar -zxvi -f - -C .
```

Then run FastViromeExplorer-annotate using the following commands:
```bash
mkdir /path-to-results/FVE-annotate-outputDirectory
cd FVE-annotate-project-directory
javac -d bin src/*.java
java -cp bin FVEAnnotate -in /path-to-FVE-novel-results/final-scaffolds.fasta -blastDbDir /path-to-blast-db-directory -o /path-to-results/FVE-annotate-outputDirectory
```

# Output
The ouput folders and files will be generated in the `FVE-annotate-outputDirectory` folder. For each scaffold in final-scaffolds.fasta, a output directory containing prodigal output and blastp output will be generated.

The output files are:
1. *scaffold-num-proteins.faa* : predicted proteins from this scaffold.
2. *blastp-output.txt* : Output of Blastp tool from aligning the proteins from this scaffold against blast non-redundant protein database.  