#!/bin/bash

#Fetches the latest taxonomy dump from ncbi, and formats it for use with BBTools
#For other BBTools programs that use taxonomy, you can set "taxpath=X" where X is the location of the files generated by this script.
#Setting "taxpath=X tree=auto" will cause the program to look for the tax tree at location "X/tree.taxtree.gz".

module load pigz
wget -nv ftp://ftp.ncbi.nih.gov/pub/taxonomy/accession2taxid/*.gz

time shrinkaccession.sh in=dead_nucl.accession2taxid.gz out=shrunk.dead_nucl.accession2taxid.gz zl=8 1>s1.o 2>&1 &
time shrinkaccession.sh in=dead_prot.accession2taxid.gz out=shrunk.dead_prot.accession2taxid.gz zl=8 1>s2.o 2>&1 &
time shrinkaccession.sh in=dead_wgs.accession2taxid.gz out=shrunk.dead_wgs.accession2taxid.gz zl=8 1>s3.o 2>&1 &
time shrinkaccession.sh in=nucl_est.accession2taxid.gz out=shrunk.nucl_est.accession2taxid.gz zl=8 1>s4.o 2>&1 &
time shrinkaccession.sh in=nucl_gb.accession2taxid.gz out=shrunk.nucl_gb.accession2taxid.gz zl=8 1>s5.o 2>&1 &
time shrinkaccession.sh in=nucl_gss.accession2taxid.gz out=shrunk.nucl_gss.accession2taxid.gz zl=8 1>s6.o 2>&1 &
time shrinkaccession.sh in=nucl_wgs.accession2taxid.gz out=shrunk.nucl_wgs.accession2taxid.gz zl=8 1>s7.o 2>&1 &
time shrinkaccession.sh in=pdb.accession2taxid.gz out=shrunk.pdb.accession2taxid.gz zl=8 1>s8.o 2>&1 &
time shrinkaccession.sh in=prot.accession2taxid.gz out=shrunk.prot.accession2taxid.gz zl=8

wget -nv ftp://ftp.ncbi.nih.gov/pub/taxonomy/taxdmp.zip
wget -nv ftp://ftp.ncbi.nih.gov/pub/taxonomy/gi_taxid_nucl.dmp.gz
wget -nv ftp://ftp.ncbi.nih.gov/pub/taxonomy/gi_taxid_prot.dmp.gz

unzip taxdmp.zip
time taxtree.sh names.dmp nodes.dmp merged.dmp tree.taxtree.gz -Xmx16g
time gitable.sh gi_taxid_nucl.dmp.gz,gi_taxid_prot.dmp.gz gitable.int1d.gz -Xmx16g

#rm -f dead_nucl.accession2taxid.gz
#rm -f dead_prot.accession2taxid.gz
#rm -f dead_wgs.accession2taxid.gz
#rm -f nucl_est.accession2taxid.gz
#rm -f nucl_gb.accession2taxid.gz
#rm -f nucl_gss.accession2taxid.gz
#rm -f nucl_wgs.accession2taxid.gz
#rm -f pdb.accession2taxid.gz
#rm -f prot.accession2taxid.gz
