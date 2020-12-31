import os
import glob

def readfile(file):
    f = open(file, "r")
    text = f.read()
    f.close()
    return text

def main():
    directory = "../data/cohacorpus/"
    outdir = "../data/wordstats/"    
    owd = os.getcwd()
    os.chdir(directory)
    files = glob.glob("*.txt")
    os.chdir(owd)
    for file in files:
        print(file)
        os.system('java semsteg.WordStatistics '+directory+file+" "+outdir+file)

    directory = "../data/globwbe/"  
    owd = os.getcwd()
    os.chdir(directory)
    files = glob.glob("*.txt")
    os.chdir(owd)
    for file in files:
        print(file)
        os.system('java semsteg.WordStatistics '+directory+file+" "+outdir+file)    
        
    owd = os.getcwd()
    os.chdir(outdir)
    files = glob.glob("*.txt")
    os.chdir(owd)    
    
    foraverage = 0
    for file in files:
        content = readfile(outdir+file)
        foraverage += eval(content)
    print (foraverage/len(files))
    
main()