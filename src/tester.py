import os
import glob

def main():
    #directory = "../data/globwbe/"
    #outdir = "../data/globwbe_stats/"
    directory = "../data/cohacorpus/"
    outdir = "../data/cohacorpus_stats/"    
    owd = os.getcwd()
    os.chdir(directory)
    files = glob.glob("*.txt")
    os.chdir(owd)
    for file in files:
        print(file)
        os.system('java semsteg.ReplacementStatistics '+directory+file+" "+outdir+file)
main()